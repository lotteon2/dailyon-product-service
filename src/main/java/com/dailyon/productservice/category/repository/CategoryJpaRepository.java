package com.dailyon.productservice.category.repository;

import com.dailyon.productservice.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    boolean existsByNameAndDeletedIsFalse(String name);
    Optional<Category> findByIdAndDeletedIsFalse(Long id);
    List<Category> findCategoriesByDeletedIsFalse();
}
