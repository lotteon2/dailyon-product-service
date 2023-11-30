package com.dailyon.productservice.category.repository;

import com.dailyon.productservice.category.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    boolean isDuplicatedName(String name);
    Optional<Category> findById(Long id);
    List<Category> findAll();
    List<Category> findByMasterCategoryId(Long masterCategoryId);
}
