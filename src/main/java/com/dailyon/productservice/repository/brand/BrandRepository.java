package com.dailyon.productservice.repository.brand;

import com.dailyon.productservice.entity.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Brand save(Brand brand);

    boolean isDuplicatedName(String name);

    List<Brand> findAllBrands();

    Optional<Brand> findById(Long id);
}
