package com.dailyon.productservice.category.repository;

import com.dailyon.productservice.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category save(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public boolean isDuplicatedName(String name) {
        return categoryJpaRepository.existsByName(name);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public List<Category> findByMasterCategoryId(Long masterCategoryId) {
        return categoryJpaRepository.findByMasterCategory_Id(masterCategoryId);
    }

    @Override
    public List<Category> findLeafCategories() {
        return categoryJpaRepository.findLeafCategories();
    }

    @Override
    public List<Category> findAllChildCategories(Long categoryId) {
        return categoryJpaRepository.findAllChildCategories(categoryId);
    }

    @Override
    public Page<Category> findCategoryPages(Pageable pageable) {
        return categoryJpaRepository.findAll(pageable);
    }
}
