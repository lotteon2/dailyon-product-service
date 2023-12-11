package com.dailyon.productservice.product.facade;

import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.common.exception.DeleteException;
import com.dailyon.productservice.common.feign.client.PromotionFeignClient;
import com.dailyon.productservice.common.feign.response.CouponForProductResponse;
import com.dailyon.productservice.product.dto.UpdateProductDto;
import com.dailyon.productservice.product.dto.request.CreateProductRequest;
import com.dailyon.productservice.product.dto.request.UpdateProductRequest;
import com.dailyon.productservice.product.dto.response.*;
import com.dailyon.productservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final PromotionFeignClient promotionFeignClient;

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        return productService.createProduct(createProductRequest);
    }

    public UpdateProductResponse updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        UpdateProductDto updateProductDto = productService.updateProduct(productId, updateProductRequest);

        // TODO : notification kafka.send

        return UpdateProductResponse.create(updateProductDto);
    }

    @Transactional
    public void deleteProducts(List<Long> ids) {
        String productIds = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        throwExceptionIfCouponExists(promotionFeignClient.checkCouponExistence(productIds).getBody());
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

    public ReadProductSliceResponse readProductSlice(Long lastId, Long brandId, Long categoryId,
                                              Gender gender, ProductType productType) {
        return productService.readProductSlice(lastId, brandId, categoryId, gender, productType);
    }

    public ReadProductSliceResponse searchProductSlice(Long lastId, String query) {
        return productService.searchProductSlice(lastId, query);
    }

    public ReadOOTDSearchSliceResponse searchFromOOTD(Long lastId, String query) {
        return productService.searchFromOOTD(lastId, query);
    }

    public ReadProductPageResponse readProductPage(Long brandId, Long categoryId, ProductType type, Pageable pageable) {
        return productService.readProductPage(brandId, categoryId, type, pageable);
    }
}
