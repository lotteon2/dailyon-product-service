package com.dailyon.productservice.repository.productsize;

import com.dailyon.productservice.entity.ProductSize;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductSizeRepositoryImpl implements ProductSizeRepository {
    private final ProductSizeJpaRepository productSizeJpaRepository;

    @Override
    public boolean isDuplicatedName(String name) {
        return productSizeJpaRepository.existsByName(name);
    }

    @Override
    public ProductSize save(ProductSize productSize) {
        return productSizeJpaRepository.save(productSize);
    }
}
