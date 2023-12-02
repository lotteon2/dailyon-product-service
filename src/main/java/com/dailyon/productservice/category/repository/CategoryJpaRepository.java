package com.dailyon.productservice.category.repository;

import com.dailyon.productservice.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    boolean existsByNameAndDeletedIsFalse(String name);
    Optional<Category> findByIdAndDeletedIsFalse(Long id);
    List<Category> findByDeletedIsFalse();
    List<Category> findByDeletedIsFalseAndMasterCategory_Id(Long masterCategoryId);

    @Query(nativeQuery = true, value =
            "WITH RECURSIVE LeafCategory(id, master_category_id, name) AS (" +
                "SELECT c.id, c.master_category_id, c.name " +
                "FROM category AS c " +
                "WHERE c.id NOT IN (" +
                    "SELECT DISTINCT c.master_category_id " +
                    "FROM category AS c " +
                    "WHERE c.master_category_id IS NOT NULL AND c.is_deleted = false) " +
                "UNION ALL " +
                    "SELECT c.id, c.master_category_id, c.name " +
                    "FROM category AS c " +
                    "INNER JOIN LeafCategory AS lc ON c.master_category_id = lc.id " +
                    "WHERE c.is_deleted = false) " +
            "SELECT * FROM LeafCategory")
    List<Category> findLeafCategories();
}
