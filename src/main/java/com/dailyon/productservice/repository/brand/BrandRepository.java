package com.dailyon.productservice.repository.brand;

import com.dailyon.productservice.entity.Brand;

import java.util.List;

public interface BrandRepository {
    Brand save(Brand brand);

    boolean isDuplicatedName(String name);

    List<Brand> findAllBrands();
}
