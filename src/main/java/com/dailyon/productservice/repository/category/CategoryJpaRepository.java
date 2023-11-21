package com.dailyon.productservice.repository.category;

import com.dailyon.productservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    Optional<Category> findByIdAndDeletedIsFalse(Long id);
    @Query(value = "SELECT child " +
            "FROM Category child " +
            "JOIN FETCH child.masterCategory parent " +
            "WHERE parent.id = :id AND parent.deleted = false AND child.deleted = false")
    List<Category> findChildrenCategoriesById(Long id);
    List<Category> findCategoriesByDeletedIsFalse();
}
