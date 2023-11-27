package com.dailyon.productservice.productstock.repository;

import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.productstock.entity.ProductStock;

import java.util.List;

public interface ProductStockRepository {
    List<ProductStock> saveAll(List<ProductStock> productStocks);
    List<ProductStock> findProductsByProduct(Product product);
    void deleteByProduct(Product product);
}
