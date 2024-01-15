package com.dailyon.productservice.category.repository;

import com.dailyon.productservice.category.entity.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    List<Category> findByMasterCategory_Id(Long masterCategoryId);

    @Query(nativeQuery = true, value =
            "WITH RECURSIVE LeafCategory(id) AS (" +
                    "SELECT c.id " +
                    "FROM category AS c " +
                    "WHERE c.id NOT IN (" +
                        "SELECT DISTINCT c.master_category_id " +
                        "FROM category AS c " +
                        "WHERE c.master_category_id IS NOT NULL AND c.is_deleted = false) " +
                    "UNION ALL " +
                    "SELECT c.id " +
                    "FROM category AS c " +
                    "INNER JOIN LeafCategory AS lc ON c.master_category_id = lc.id " +
                    "WHERE c.is_deleted = false) " +
            "SELECT c.* FROM category AS c " +
            "INNER JOIN LeafCategory AS ct ON c.id = ct.id AND " +
            "c.is_deleted = false")
    List<Category> findLeafCategories();

    @Query(nativeQuery = true, value =
            "WITH RECURSIVE CategoryTree(id) AS (" +
                    "SELECT c.id " +
                    "FROM category AS c " +
                    "WHERE c.id = :categoryId AND c.is_deleted = false " +
                    "UNION ALL " +
                    "SELECT c.id " +
                    "FROM category AS c " +
                    "INNER JOIN CategoryTree AS ct " +
                    "ON c.master_category_id = ct.id " +
                    "WHERE c.is_deleted = false)" +
            "SELECT c.* FROM category AS c " +
            "INNER JOIN CategoryTree AS ct ON c.id = ct.id AND " +
            "c.is_deleted = false")
    List<Category> findAllChildCategories(@Param("categoryId") Long categoryId);
}
