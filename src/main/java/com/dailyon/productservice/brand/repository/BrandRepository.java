package com.dailyon.productservice.brand.repository;

import com.dailyon.productservice.brand.entity.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Brand save(Brand brand);

    boolean isDuplicatedName(String name);

    List<Brand> findAllBrands();

    Optional<Brand> findById(Long id);
}
