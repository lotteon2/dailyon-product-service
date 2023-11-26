package com.dailyon.productservice.brand.repository;

import com.dailyon.productservice.brand.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {

    private final BrandJpaRepository brandJpaRepository;
    @Override
    public Brand save(Brand brand) {
        return brandJpaRepository.save(brand);
    }

    @Override
    public boolean isDuplicatedName(String name) {
        return brandJpaRepository.existsByNameAndDeletedIsFalse(name);
    }

    @Override
    public List<Brand> findAllBrands() {
        return brandJpaRepository.findAllByDeletedIsFalse();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return brandJpaRepository.findByIdAndDeletedIsFalse(id);
    }
}