package com.dailyon.productservice.productstock.repository;

import com.dailyon.productservice.common.feign.request.OrderProductDto;
import com.dailyon.productservice.productstock.entity.ProductStock;

import java.util.List;


public interface ProductStockCustomRepository {

    List<ProductStock> findOrderProductsBy(List<OrderProductDto> productDtos);
}
