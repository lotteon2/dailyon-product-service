package com.dailyon.productservice.repository.productsize;

import com.dailyon.productservice.entity.ProductSize;

public interface ProductSizeRepository {
    boolean isDuplicatedName(String name);
    ProductSize save(ProductSize productSize);
}
