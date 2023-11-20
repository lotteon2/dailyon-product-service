package com.dailyon.productservice.service;

import com.dailyon.productservice.dto.request.CreateBrandRequest;
import com.dailyon.productservice.dto.response.CreateBrandResponse;
import com.dailyon.productservice.entity.Brand;
import com.dailyon.productservice.exception.DuplicatedBrandException;
import com.dailyon.productservice.repository.brand.BrandRepository;
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
            throw new DuplicatedBrandException("이미 존재하는 브랜드 이름입니다");
        }

        // 이후 save
        Brand brand = brandRepository.save(Brand.createBrand(createBrandRequest.getName()));

        return CreateBrandResponse.builder()
                .brandId(brand.getId())
                .build();
    }
}
