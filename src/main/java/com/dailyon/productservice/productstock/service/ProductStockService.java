package com.dailyon.productservice.productstock.service;

import com.dailyon.productservice.product.dto.request.ReadOrderProductRequest;
import com.dailyon.productservice.product.dto.response.ReadOrderProductListResponse;
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

    public ReadOrderProductListResponse readOrderProducts(ReadOrderProductRequest request) {
        List<ReadOrderProductRequest.ProductDto> dtoList = request.getProductRequest();
        return ReadOrderProductListResponse.fromEntity(productStockRepository.findOrderProductsBy(dtoList));
    }
}
