package com.dailyon.productservice.service.category;

import com.dailyon.productservice.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.exception.UniqueException;
import com.dailyon.productservice.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category createCategory(CreateCategoryRequest createCategoryRequest) {
        // 이미 존재하는 카테고리명이라면 exception
        if(categoryRepository.isDuplicatedName(createCategoryRequest.getCategoryName())) {
            throw new UniqueException();
        }

        // request body에 masterCategoryId가 없다면 최상위 카테고리 생성
        if(createCategoryRequest.getMasterCategoryId() == null) {
            return categoryRepository.save(Category.createRootCategory(createCategoryRequest.getCategoryName()));
        }

        // request body에 masterCategoryId가 있다면 하위 카테고리 생성
        Optional<Category> masterCategory = categoryRepository.findById(createCategoryRequest.getMasterCategoryId());
        if(masterCategory.isEmpty()) { // masterCategoryId로 masterCategory 조회했을 때 존재하지 않는다면 exception
            throw new NotExistsException();
        }
        return categoryRepository.save(Category.createCategory(masterCategory.get(), createCategoryRequest.getCategoryName()));
    }
}
