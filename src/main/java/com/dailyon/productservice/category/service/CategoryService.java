package com.dailyon.productservice.category.service;

import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.dto.request.UpdateCategoryRequest;
import com.dailyon.productservice.category.dto.response.CreateCategoryResponse;
import com.dailyon.productservice.category.dto.response.ReadAllCategoryListResponse;
import com.dailyon.productservice.category.dto.response.ReadBreadCrumbListResponse;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        // 이미 존재하는 카테고리명이라면 exception
        if(categoryRepository.isDuplicatedName(createCategoryRequest.getCategoryName())) {
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
        return ReadChildrenCategoryListResponse.fromEntity(categoryRepository.findByMasterCategoryId(masterCategoryId));
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

        if(categoryRepository.isDuplicatedName(updateCategoryRequest.getName())) {
            throw new UniqueException(UniqueException.DUPLICATE_CATEGORY_NAME);
        }

        category.updateName(updateCategoryRequest.getName());
    }

    public ReadChildrenCategoryListResponse readLeafCategories() {
        return ReadChildrenCategoryListResponse.fromEntity(categoryRepository.findLeafCategories());
    }
}