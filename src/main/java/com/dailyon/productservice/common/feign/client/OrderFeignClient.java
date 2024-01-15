package com.dailyon.productservice.common.feign.client;

import com.dailyon.productservice.common.config.FeignClientConfig;
import dailyon.domain.order.clients.ProductRankResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@FeignClient(
        name = "orderFeignClient",
        url = "${endpoint.order-service}",
        configuration = FeignClientConfig.class
)
public interface OrderFeignClient {
    @CircuitBreaker(name = "orderFeignClient@getBestProductInfo",
                    fallbackMethod = "getBestProductInfoFallback")
    @GetMapping("/clients/orders/ranks")
    ResponseEntity<List<ProductRankResponse>> getBestProductInfo();

    default ResponseEntity<List<ProductRankResponse>> getBestProductInfoFallback(Throwable t) {
        return ResponseEntity.ok(new ArrayList<>());
    }
}
