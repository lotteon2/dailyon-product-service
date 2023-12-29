package com.dailyon.productservice.brand.service;

import com.dailyon.productservice.brand.dto.request.CreateBrandRequest;
import com.dailyon.productservice.brand.dto.request.UpdateBrandRequest;
import com.dailyon.productservice.brand.dto.response.CreateBrandResponse;
import com.dailyon.productservice.brand.dto.response.ReadBrandListResponse;
import com.dailyon.productservice.brand.dto.response.ReadBrandPageResponse;
import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.common.exception.DeleteException;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CreateBrandResponse createBrand(CreateBrandRequest createBrandRequest) {
        // 이미 존재하는 브랜드 이름이라면 exception
        if(brandRepository.existsByName(createBrandRequest.getName())) {
            throw new UniqueException(UniqueException.DUPLICATE_BRAND_NAME);
        }

        // 이후 save
        Brand brand = brandRepository.save(Brand.createBrand(createBrandRequest.getName()));
        return CreateBrandResponse.fromEntity(brand);
    }

    public ReadBrandListResponse readAllBrands() {
        return ReadBrandListResponse.fromEntity(brandRepository.findAll());
    }

    @Transactional
    public void updateBrand(Long id, UpdateBrandRequest updateBrandRequest) {
        // 존재하지 않는 id에 대해 수정하려고 하면 exception
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotExistsException(NotExistsException.BRAND_NOT_FOUND));
        // 이미 존재하는 브랜드 이름이라면 exception
        if(brandRepository.existsByName(updateBrandRequest.getName())) {
            throw new UniqueException(UniqueException.DUPLICATE_BRAND_NAME);
        }
        brand.setName(updateBrandRequest.getName());
    }

    public ReadBrandPageResponse readBrandPage(Pageable pageable) {
        return ReadBrandPageResponse.fromEntity(brandRepository.findAll(pageable));
    }

    @Transactional
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotExistsException(NotExistsException.BRAND_NOT_FOUND));
        if(productRepository.existsProductByBrand(brand)) {
            throw new DeleteException(DeleteException.BRAND_PRODUCT_EXISTS);
        }
        brand.softDelete();
    }
}
