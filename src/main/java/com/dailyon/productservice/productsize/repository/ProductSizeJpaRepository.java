package com.dailyon.productservice.productsize.repository;

import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.productsize.entity.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductSizeJpaRepository extends JpaRepository<ProductSize, Long> {
    boolean existsByCategoryAndName(Category category, String name);
    @Query(value = "SELECT ps " +
            "FROM ProductSize ps " +
            "JOIN FETCH ps.category c " +
            "WHERE c.id = :id AND c.deleted = false AND ps.deleted = false")
    List<ProductSize> findProductSizesByCategoryId(Long id);

    @Query(value = "SELECT ps " +
            "FROM ProductSize ps " +
            "WHERE ps.id IN :productSizeIds AND ps.deleted = false ")
    List<ProductSize> findProductSizesByIds(Set<Long> productSizeIds);

    Optional<ProductSize> findProductSizeById(Long id);

    @Query(value = "SELECT ps " +
            "FROM ProductSize AS ps " +
            "LEFT JOIN ps.category AS c " +
            "WHERE c.id = :categoryId AND c.deleted = false AND ps.deleted = false")
    Page<ProductSize> findProductSizePagesByCategoryId(Long categoryId, Pageable pageable);

    @Modifying(flushAutomatically = true)
    @Query(value = "UPDATE ProductSize AS ps SET ps.deleted = true WHERE ps.category IN :category")
    void deleteProductSizesByCategory(List<Category> category);
}
