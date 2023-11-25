package com.dailyon.productservice.brand.service;

import com.dailyon.productservice.brand.dto.request.CreateBrandRequest;
import com.dailyon.productservice.brand.dto.request.UpdateBrandRequest;
import com.dailyon.productservice.brand.dto.response.CreateBrandResponse;
import com.dailyon.productservice.brand.dto.response.ReadBrandListResponse;
import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional
    public CreateBrandResponse createBrand(CreateBrandRequest createBrandRequest) {
        // 이미 존재하는 브랜드 이름이라면 exception
        if(brandRepository.isDuplicatedName(createBrandRequest.getName())) {
            throw new UniqueException(UniqueException.DUPLICATE_BRAND_NAME);
        }

        // 이후 save
        Brand brand = brandRepository.save(Brand.createBrand(createBrandRequest.getName()));

        return CreateBrandResponse.builder()
                .brandId(brand.getId())
                .build();
    }

    public ReadBrandListResponse readAllBrands() {
        return ReadBrandListResponse.fromEntity(brandRepository.findAllBrands());
    }

    @Transactional
    public void updateBrand(Long id, UpdateBrandRequest updateBrandRequest) {
        // 존재하지 않는 id에 대해 수정하려고 하면 exception
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotExistsException(NotExistsException.BRAND_NOT_FOUND));
        // 이미 존재하는 브랜드 이름이라면 exception
        if(brandRepository.isDuplicatedName(updateBrandRequest.getName())) {
            throw new UniqueException(UniqueException.DUPLICATE_BRAND_NAME);
        }
        brand.updateName(updateBrandRequest.getName());
    }
}
