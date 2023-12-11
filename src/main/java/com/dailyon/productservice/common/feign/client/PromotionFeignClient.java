package com.dailyon.productservice.common.feign.client;

import com.dailyon.productservice.common.config.FeignClientConfig;
import com.dailyon.productservice.common.feign.response.CouponForProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
    name = "promotion-service",
    configuration = FeignClientConfig.class
)
public interface PromotionFeignClient {
    @GetMapping(value = "/clients/coupons/coupon-existence", params = "productIds")
    ResponseEntity<List<CouponForProductResponse>> checkCouponExistence(@RequestParam(name = "productIds") String productIds);
}
