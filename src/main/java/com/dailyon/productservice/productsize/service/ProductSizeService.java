package com.dailyon.productservice.productsize.service;

import com.dailyon.productservice.productsize.dto.request.CreateProductSizeRequest;
import com.dailyon.productservice.productsize.dto.request.UpdateProductSizeRequest;
import com.dailyon.productservice.productsize.dto.response.CreateProductSizeResponse;
import com.dailyon.productservice.productsize.dto.response.ReadProductSizeListResponse;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.productsize.dto.response.ReadProductSizePageResponse;
import com.dailyon.productservice.productsize.entity.ProductSize;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductSizeService {
    private final ProductSizeRepository productSizeRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CreateProductSizeResponse createProductSize(CreateProductSizeRequest createProductSizeRequest) {
        Category category = categoryRepository
                .findById(createProductSizeRequest.getCategoryId())
                .orElseThrow(() -> new NotExistsException(NotExistsException.CATEGORY_NOT_FOUND));

        if(productSizeRepository.existsByCategoryAndName(category, createProductSizeRequest.getName())) {
            throw new UniqueException(UniqueException.DUPLICATE_PRODUCT_SIZE_NAME);
        }

        return CreateProductSizeResponse
                .fromEntity(productSizeRepository.save(ProductSize.create(category, createProductSizeRequest.getName())));
    }

    public ReadProductSizeListResponse readProductSizeListByCategory(Long id) {
        return ReadProductSizeListResponse.fromEntity(productSizeRepository.findProductSizesByCategoryId(id));
    }

    @Transactional
    public void updateProductSizeName(Long productSizeId, UpdateProductSizeRequest updateProductSizeRequest) {
        // 존재하지 않는 product size를 수정하려고 하면 exception
        ProductSize productSize = productSizeRepository.findProductSizeById(productSizeId)
                .orElseThrow(() -> new NotExistsException(NotExistsException.PRODUCT_SIZE_NOT_FOUND));


        // 이미 존재하는 치수값이라면 exception
        if(productSizeRepository.existsByCategoryAndName(productSize.getCategory(), updateProductSizeRequest.getName())) {
            throw new UniqueException(UniqueException.DUPLICATE_PRODUCT_SIZE_NAME);
        }

        productSize.updateName(updateProductSizeRequest.getName());
    }

    public ReadProductSizePageResponse readProductSizePage(Long categoryId, Pageable pageable) {
        return ReadProductSizePageResponse
                .fromEntity(productSizeRepository.findProductSizePagesByCategoryId(categoryId, pageable));
    }
}
