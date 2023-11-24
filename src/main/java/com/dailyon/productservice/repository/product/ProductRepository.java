package com.dailyon.productservice.repository.product;

import com.dailyon.productservice.entity.Product;

public interface ProductRepository {
    Product save(Product product);
}
