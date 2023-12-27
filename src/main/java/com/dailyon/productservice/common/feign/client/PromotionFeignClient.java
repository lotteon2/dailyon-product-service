package com.dailyon.productservice.common.feign.client;

import com.dailyon.productservice.common.config.FeignClientConfig;
import com.dailyon.productservice.common.feign.request.MultipleProductCouponsRequest;
import com.dailyon.productservice.common.feign.response.CouponForProductResponse;
import com.dailyon.productservice.common.feign.response.MultipleProductCouponsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "promotion-service",
        url = "${endpoint.promotion-service}",
        configuration = FeignClientConfig.class
)
public interface PromotionFeignClient {
    @GetMapping(value = "/clients/coupons/coupons-existence")
    ResponseEntity<List<CouponForProductResponse>> checkCouponExistence(@RequestParam(name = "productIds") List<Long> productIds);

    @PostMapping(value = "/clients/coupons/multiple-products")
    ResponseEntity<MultipleProductCouponsResponse> getCouponsForProducts(@RequestBody MultipleProductCouponsRequest request);
}
