package com.dailyon.productservice.product.facade;

import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.common.exception.DeleteException;
import com.dailyon.productservice.common.feign.client.OrderFeignClient;
import com.dailyon.productservice.common.feign.client.PromotionFeignClient;
import com.dailyon.productservice.common.feign.request.MultipleProductCouponsRequest;
import com.dailyon.productservice.common.feign.response.CouponForProductResponse;
import com.dailyon.productservice.common.feign.response.MultipleProductCouponsResponse;
import com.dailyon.productservice.product.dto.UpdateProductDto;
import com.dailyon.productservice.product.dto.request.CreateProductRequest;
import com.dailyon.productservice.product.dto.request.UpdateProductRequest;
import com.dailyon.productservice.product.dto.response.*;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.service.ProductService;
import com.dailyon.productservice.product.sqs.ProductRestockHandler;
import dailyon.domain.order.clients.ProductRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final PromotionFeignClient promotionFeignClient;
    private final OrderFeignClient orderFeignClient;
    private final ProductRestockHandler productRestockHandler;

    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        return productService.createProduct(createProductRequest);
    }

    public UpdateProductResponse updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        UpdateProductDto updateProductDto = productService.updateProduct(productId, updateProductRequest);
        productRestockHandler.produce(updateProductDto.getProductStocksToNotify());
        return UpdateProductResponse.create(updateProductDto);
    }

    public void deleteProducts(List<Long> ids) {
        throwExceptionIfCouponExists(promotionFeignClient.checkCouponExistence(ids).getBody());
        productService.deleteProductsByIds(ids);
    }

    private void throwExceptionIfCouponExists(List<CouponForProductResponse> body) {
        List<String> couponExists = new ArrayList<>();
        for(CouponForProductResponse product: body) {
            if(product.isHasAvailableCoupon()) {
                couponExists.add(String.valueOf(product.getProductId()));
            }
        }

        if(!couponExists.isEmpty()) {
            throw new DeleteException(String.join(", ", couponExists) + DeleteException.COUPON_EXISTS);
        }
    }

    public ReadProductDetailResponse readProductDetail(Long productId) {
        return productService.readProductDetail(productId);
    }

    public ReadProductSliceResponse readProductSlice(
            String lastVal, Long brandId, Long categoryId, Gender gender, ProductType productType,
            Integer lowPrice, Integer highPrice, String query, String sort, String direction
    ) {
        Slice<Product> products = productService.readProductSlice(
                lastVal, brandId, categoryId, gender, productType,
                lowPrice, highPrice, query, sort, direction);

        MultipleProductCouponsResponse response = promotionFeignClient.getCouponsForProducts(
                MultipleProductCouponsRequest.fromEntity(products.getContent())
        ).getBody();

        return ReadProductSliceResponse.create(products, response.getCoupons());
    }

    public ReadOOTDSearchSliceResponse searchFromOOTD(Long lastId, String query) {
        return productService.searchFromOOTD(lastId, query);
    }

    public ReadProductPageResponse readProductPage(
            Long brandId, Long categoryId, ProductType type, String query,
            int page, int size, String sort, String direction
    ) {
        return productService.readProductPage(
                brandId, categoryId, type, query,
                page, size, sort, direction
        );
    }

    @Cacheable(value = "newProducts", unless = "#result == null")
    public ReadNewProductListResponse readNewProducts() {
        return productService.readNewProducts();
    }

    @Cacheable(value = "bestProducts", unless = "#result == null")
    public ReadBestProductListResponse readBestProducts() {
        /*
         O(N^2)으로 순회해서 매칭시키지 않기 위해
         상품과 최다 판매 상품 둘 다 id 오름차순으로 정렬(NlogN)해서 매칭
         이후 판매량 순으로 순위 매기기 위해 내림차순 정렬
         */

        List<ProductRankResponse> ranks = orderFeignClient.getBestProductInfo().getBody();
        assert ranks != null; // since circuit breaker fallback method
        ranks.sort(Comparator.comparing(ProductRankResponse::getProductId));

        List<Long> ids = ranks.stream()
                .map(ProductRankResponse::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productService.readBestProducts(ids);
        products.sort(Comparator.comparing(Product::getId));

        return ReadBestProductListResponse.create(products, ranks);
    }
}
