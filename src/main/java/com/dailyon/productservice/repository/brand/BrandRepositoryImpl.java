package com.dailyon.productservice.repository.brand;

import com.dailyon.productservice.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
