package com.dailyon.productservice.category.service;

import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.dto.request.UpdateCategoryRequest;
import com.dailyon.productservice.category.dto.response.*;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.common.exception.DeleteException;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.product.repository.ProductRepository;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;

    @Transactional
    public CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        // 이미 존재하는 카테고리명이라면 exception
        if(categoryRepository.existsByName(createCategoryRequest.getCategoryName())) {
            throw new UniqueException(UniqueException.DUPLICATE_CATEGORY_NAME);
        }

        // request body에 masterCategoryId가 없다면 최상위 카테고리 생성
        if(createCategoryRequest.getMasterCategoryId() == null) {
            return CreateCategoryResponse
                    .fromEntity(categoryRepository.save(Category.createRootCategory(createCategoryRequest.getCategoryName())));
        }

        // request body에 masterCategoryId가 있다면 하위 카테고리 생성
        Category masterCategory = categoryRepository.findById(createCategoryRequest.getMasterCategoryId())
                .orElseThrow(() -> new NotExistsException(NotExistsException.CATEGORY_NOT_FOUND));

        return CreateCategoryResponse
                .fromEntity(categoryRepository.save(Category.createCategory(masterCategory, createCategoryRequest.getCategoryName())));
    }

    public ReadChildrenCategoryListResponse readChildrenCategoriesByMaster(Long masterCategoryId) {
        return ReadChildrenCategoryListResponse.fromEntity(categoryRepository.findByMasterCategory_Id(masterCategoryId));
    }

    public ReadAllCategoryListResponse readAllCategories() {
        return ReadAllCategoryListResponse.fromEntity(categoryRepository.findAll());
    }

    public ReadBreadCrumbListResponse readBreadCrumbs(Long id) {
        Category self = categoryRepository.findById(id)
                .orElseThrow(() -> new NotExistsException(NotExistsException.CATEGORY_NOT_FOUND));
        return ReadBreadCrumbListResponse.fromEntity(self.readBreadCrumbs());
    }

    @Transactional
    public void updateCategoryName(Long id, UpdateCategoryRequest updateCategoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotExistsException(NotExistsException.CATEGORY_NOT_FOUND));

        if(categoryRepository.existsByName(updateCategoryRequest.getName())) {
            throw new UniqueException(UniqueException.DUPLICATE_CATEGORY_NAME);
        }

        category.setName(updateCategoryRequest.getName());
    }

    public ReadChildrenCategoryListResponse readLeafCategories() {
        return ReadChildrenCategoryListResponse.fromEntity(categoryRepository.findLeafCategories());
    }

    public ReadCategoryPageResponse readCategoryPages(Pageable pageable) {
        return ReadCategoryPageResponse.fromEntity(categoryRepository.findAll(pageable));
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotExistsException(NotExistsException.CATEGORY_NOT_FOUND));

        List<Category> categories = categoryRepository.findAllChildCategories(categoryId);
        if(!productRepository.findProductsFromCategories(categories).isEmpty()) {
            throw new DeleteException(DeleteException.CATEGORY_PRODUCT_EXISTS);
        }

        categories.forEach(Category::softDelete);
        productSizeRepository.deleteProductSizesByCategory(categories);
    }
}