package com.dailyon.productservice.repository.product;

import com.dailyon.productservice.entity.Product;

import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> getProductDetail(Long productId);
}
