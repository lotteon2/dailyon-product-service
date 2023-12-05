package com.dailyon.productservice.productstock.repository;

import com.dailyon.productservice.product.dto.request.ReadOrderProductRequest;
import com.dailyon.productservice.productstock.entity.ProductStock;

import java.util.List;


public interface ProductStockCustomRepository {

    List<ProductStock> findOrderProductsBy(List<ReadOrderProductRequest.ProductDto> productDtos);
}
