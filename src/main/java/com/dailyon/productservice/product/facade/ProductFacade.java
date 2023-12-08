package com.dailyon.productservice.product.facade;

import com.dailyon.productservice.common.feign.client.PromotionFeignClient;
import com.dailyon.productservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductFacade {
    private final ProductService productService;
    private final PromotionFeignClient promotionFeignClient;


}
