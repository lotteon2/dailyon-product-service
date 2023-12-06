package com.dailyon.productservice.productstock.service;

import com.dailyon.productservice.common.feign.request.OrderProductDto;
import com.dailyon.productservice.common.feign.response.ReadOrderProductListResponse;
import com.dailyon.productservice.productstock.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductStockService {
    private final ProductStockRepository productStockRepository;

    public ReadOrderProductListResponse readOrderProducts(List<OrderProductDto> request) {
        return ReadOrderProductListResponse.fromEntity(productStockRepository.findOrderProductsBy(request));
    }
}
