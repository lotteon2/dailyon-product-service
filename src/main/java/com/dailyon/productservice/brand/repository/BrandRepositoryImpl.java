package com.dailyon.productservice.brand.repository;

import com.dailyon.productservice.brand.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return brandJpaRepository.existsByName(name);
    }

    @Override
    public List<Brand> findAllBrands() {
        return brandJpaRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return brandJpaRepository.findById(id);
    }

    @Override
    public Page<Brand> readBrandPages(Pageable pageable) {
        return brandJpaRepository.findAll(pageable);
    }
}
