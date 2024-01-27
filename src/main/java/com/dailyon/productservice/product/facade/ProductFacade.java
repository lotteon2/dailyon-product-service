package com.dailyon.productservice.product.facade;

import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.common.exception.DeleteException;
import com.dailyon.productservice.common.feign.client.OpenAIClient;
import com.dailyon.productservice.common.feign.client.OrderFeignClient;
import com.dailyon.productservice.common.feign.client.PromotionFeignClient;
import com.dailyon.productservice.common.feign.request.MultipleProductCouponsRequest;
import com.dailyon.productservice.common.feign.response.CouponForProductResponse;
import com.dailyon.productservice.common.feign.response.MultipleProductCouponsResponse;
import com.dailyon.productservice.common.feign.response.OpenAIResponse;
import com.dailyon.productservice.product.dto.UpdateProductDto;
import com.dailyon.productservice.product.dto.request.CreateProductRequest;
import com.dailyon.productservice.product.dto.request.UpdateProductRequest;
import com.dailyon.productservice.product.dto.response.*;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.service.ProductService;
import com.dailyon.productservice.product.sqs.ProductRestockHandler;
import com.google.gson.Gson;
import dailyon.domain.order.clients.ProductRankResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final PromotionFeignClient promotionFeignClient;
    private final OrderFeignClient orderFeignClient;
    private final ProductRestockHandler productRestockHandler;
    private final OpenAIClient openAIClient;
    private final Gson gson;

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

    @Cacheable(value = "auctionProducts", key = "#productId", unless = "#result == null")
    public ReadProductDetailResponse readAuctionProductDetail(Long productId) {
        return productService.readProductDetail(productId);
    }

    public ReadProductSliceResponse readProductSlice(
            Long brandId, Long categoryId, Gender gender, ProductType productType,
            Integer lowPrice, Integer highPrice, String query, int page, String sort, String direction
    ) {
        Slice<Product> products = productService.readProductSlice(
                brandId, categoryId, gender, productType,
                lowPrice, highPrice, query, page, sort, direction
        );

        MultipleProductCouponsResponse response = promotionFeignClient.getCouponsForProducts(
                MultipleProductCouponsRequest.fromEntity(products.getContent())
        ).getBody();

        return ReadProductSliceResponse.create(products, response.getCoupons());
    }

    public ReadProductSearchResponse searchProducts(String query) {
        List<Product> products = productService.searchProducts(query);

        if (products.isEmpty()) {
            try {
                String translatedResponse = openAIClient.getTranslatedPrompt(query);
                OpenAIResponse translatedContent = gson.fromJson(translatedResponse, OpenAIResponse.class);
                String translatedQuery = translatedContent.getChoices().get(0).getMessage().getContent();
                log.info("translatedQuery: "+translatedQuery);

                String response = openAIClient.getSearchResults(translatedQuery);
                OpenAIResponse responseFromGpt = gson.fromJson(response, OpenAIResponse.class);
                String jsonContent = responseFromGpt.getChoices().get(0).getMessage().getContent();

                OpenAIResponse.Content content = gson.fromJson(jsonContent, OpenAIResponse.Content.class);

                // Use content object to search products
                List<Long> brandIds = content.getBrands().stream()
                        .map(OpenAIResponse.ReadBrandResponse::getId)
                        .collect(Collectors.toList());

                List<Long> categoryIds = content.getCategories().stream()
                        .map(OpenAIResponse.ReadChildrenCategoryResponse::getId)
                        .collect(Collectors.toList());

                List<Gender> genders = content.getGenders();

                Integer[] prices = new Integer[2];
                if(content.getPriceRanges().get(0) != null) {
                    prices = content.getPriceRanges().get(0).parseHighAndLow();
                }

                products = productService.searchAfterGpt(categoryIds, brandIds, genders, prices[0], prices[1]);
            } catch (Exception e) {
                // Properly log and handle the exception as per your application's requirements
                e.printStackTrace();
            }
        }

        // Assuming promotionFeignClient is correctly set up to fetch coupons
        MultipleProductCouponsResponse response = promotionFeignClient.getCouponsForProducts(
                MultipleProductCouponsRequest.fromEntity(products)
        ).getBody();

        // Return results with attached coupons
        return ReadProductSearchResponse.create(products, response.getCoupons());
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
