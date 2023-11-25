package com.dailyon.productservice.service.category;

import com.dailyon.productservice.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.dto.request.UpdateCategoryRequest;
import com.dailyon.productservice.dto.response.ReadAllCategoryListResponse;
import com.dailyon.productservice.dto.response.ReadBreadCrumbListResponse;
import com.dailyon.productservice.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.exception.UniqueException;
import com.dailyon.productservice.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category createCategory(CreateCategoryRequest createCategoryRequest) {
        // 이미 존재하는 카테고리명이라면 exception
        if(categoryRepository.isDuplicatedName(createCategoryRequest.getCategoryName())) {
            throw new UniqueException(UniqueException.DUPLICATE_CATEGORY_NAME);
        }

        // request body에 masterCategoryId가 없다면 최상위 카테고리 생성
        if(createCategoryRequest.getMasterCategoryId() == null) {
            return categoryRepository.save(Category.createRootCategory(createCategoryRequest.getCategoryName()));
        }

        // request body에 masterCategoryId가 있다면 하위 카테고리 생성
        Category masterCategory = categoryRepository.findById(createCategoryRequest.getMasterCategoryId())
                .orElseThrow(() -> new NotExistsException(NotExistsException.CATEGORY_NOT_FOUND));
        return categoryRepository.save(Category.createCategory(masterCategory, createCategoryRequest.getCategoryName()));
    }

    public ReadChildrenCategoryListResponse readChildrenCategories(Long id) {
        // 존재하지 않는 id의 하위 카테고리를 조회하려고 하면 exception
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotExistsException(NotExistsException.CATEGORY_NOT_FOUND));
        return ReadChildrenCategoryListResponse.fromEntity(category.getChildrenCategories());
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
}