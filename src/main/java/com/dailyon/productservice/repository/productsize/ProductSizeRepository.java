package com.dailyon.productservice.repository.productsize;

import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.entity.ProductSize;

public interface ProductSizeRepository {
    boolean isDuplicated(Category category, String name);
    ProductSize save(ProductSize productSize);
}
