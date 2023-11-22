package com.dailyon.productservice.repository.productsize;

import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.entity.ProductSize;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductSizeRepositoryImpl implements ProductSizeRepository {
    private final ProductSizeJpaRepository productSizeJpaRepository;

    @Override
    public boolean isDuplicated(Category category, String name) {
        return productSizeJpaRepository.existsByCategoryAndNameAndDeletedIsFalse(category, name);
    }

    @Override
    public ProductSize save(ProductSize productSize) {
        return productSizeJpaRepository.save(productSize);
    }
}
