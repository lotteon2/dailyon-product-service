package com.dailyon.productservice.productsize.repository;

import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.productsize.entity.ProductSize;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ProductSizeRepositoryImpl implements ProductSizeRepository {
    private final ProductSizeJpaRepository productSizeJpaRepository;

    @Override
    public boolean isDuplicated(Category category, String name) {
        return productSizeJpaRepository.existsByCategoryAndName(category, name);
    }

    @Override
    public ProductSize save(ProductSize productSize) {
        return productSizeJpaRepository.save(productSize);
    }

    @Override
    public List<ProductSize> readProductSizesByCategoryId(Long id) {
        return productSizeJpaRepository.findProductSizesByCategoryId(id);
    }

    @Override
    public List<ProductSize> readProductSizesByProductSizeIds(Set<Long> productSizeIds) {
        return productSizeJpaRepository.findProductSizesByIds(productSizeIds);
    }

    @Override
    public Optional<ProductSize> readProductSizeById(Long id) {
        return productSizeJpaRepository.findProductSizeById(id);
    }

    @Override
    public Page<ProductSize> readProductSizePagesByCategoryId(Long categoryId, Pageable pageable) {
        return productSizeJpaRepository.findProductSizePagesByCategoryId(categoryId, pageable);
    }
}
