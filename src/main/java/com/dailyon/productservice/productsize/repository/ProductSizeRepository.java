package com.dailyon.productservice.productsize.repository;

import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.productsize.entity.ProductSize;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductSizeRepository {
    boolean isDuplicated(Category category, String name);
    ProductSize save(ProductSize productSize);
    List<ProductSize> readProductSizesByCategoryId(Long id);
    List<ProductSize> readProductSizesByProductSizeIds(Set<Long> productSizeIds);
    Optional<ProductSize> readProductSizeById(Long id);
}
