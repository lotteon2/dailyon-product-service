package com.dailyon.productservice.repository.productstock;

import com.dailyon.productservice.entity.ProductStock;

import java.util.List;

public interface ProductStockRepository {
    List<ProductStock> saveAll(List<ProductStock> productStocks);
}
