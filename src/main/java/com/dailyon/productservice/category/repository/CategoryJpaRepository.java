package com.dailyon.productservice.category.repository;

import com.dailyon.productservice.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    List<Category> findByMasterCategory_Id(Long masterCategoryId);

    @Query(nativeQuery = true, value =
            "WITH RECURSIVE LeafCategory(id, master_category_id, name, updated_at, created_at, is_deleted) AS (" +
                "SELECT c.id, c.master_category_id, c.name, c.updated_at, c.created_at, c.is_deleted " +
                "FROM category AS c " +
                "WHERE c.id NOT IN (" +
                    "SELECT DISTINCT c.master_category_id " +
                    "FROM category AS c " +
                    "WHERE c.master_category_id IS NOT NULL AND c.is_deleted = false) " +
                "UNION ALL " +
                    "SELECT c.id, c.master_category_id, c.name, c.updated_at, c.created_at, c.is_deleted " +
                    "FROM category AS c " +
                    "INNER JOIN LeafCategory AS lc ON c.master_category_id = lc.id " +
                    "WHERE c.is_deleted = false) " +
            "SELECT * FROM LeafCategory")
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
    List<Category> findAllChildCategories(Long categoryId);
}
