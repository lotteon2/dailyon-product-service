package com.dailyon.productservice.repository.category;

import com.dailyon.productservice.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    boolean isDuplicatedName(String name);
    Optional<Category> findById(Long id);
    List<Category> findAll();
}
