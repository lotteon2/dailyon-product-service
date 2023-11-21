package com.dailyon.productservice.service.brand;

import com.dailyon.productservice.dto.request.CreateBrandRequest;
import com.dailyon.productservice.dto.request.UpdateBrandRequest;
import com.dailyon.productservice.dto.response.CreateBrandResponse;
import com.dailyon.productservice.dto.response.ReadBrandListResponse;
import com.dailyon.productservice.entity.Brand;
import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.exception.UniqueException;
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
            throw new UniqueException();
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
        Brand brand = brandRepository.findById(id).orElseThrow(NotExistsException::new);
        // 이미 존재하는 브랜드 이름이라면 exception
        if(brandRepository.isDuplicatedName(updateBrandRequest.getName())) {
            throw new UniqueException();
        }
        brand.updateName(updateBrandRequest.getName());
    }
}
