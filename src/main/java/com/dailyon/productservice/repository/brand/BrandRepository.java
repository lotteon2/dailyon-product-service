package com.dailyon.productservice.repository.brand;

import com.dailyon.productservice.entity.Brand;

public interface BrandRepository {
    Brand save(Brand brand);

    boolean isDuplicatedName(String name);
}
