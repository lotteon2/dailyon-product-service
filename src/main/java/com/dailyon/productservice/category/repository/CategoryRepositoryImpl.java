package com.dailyon.productservice.category.repository;

import com.dailyon.productservice.category.entity.Category;
import lombok.RequiredArgsConstructor;
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
        return categoryJpaRepository.findByIdAndDeletedIsFalse(id);
    }

    @Override
    public boolean isDuplicatedName(String name) {
        return categoryJpaRepository.existsByNameAndDeletedIsFalse(name);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findByDeletedIsFalse();
    }

    @Override
    public List<Category> findByMasterCategoryId(Long masterCategoryId) {
        return categoryJpaRepository.findByDeletedIsFalseAndMasterCategory_Id(masterCategoryId);
    }
}
