package com.dailyon.productservice.product.repository;

import com.dailyon.productservice.product.entity.Product;

import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> getProductDetail(Long productId);
    Optional<Product> findProductById(Long id);
}
