package com.dailyon.productservice.repository.brand;

import com.dailyon.productservice.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        return brandJpaRepository.findAllByDeletedIsFalse();
    }
}
