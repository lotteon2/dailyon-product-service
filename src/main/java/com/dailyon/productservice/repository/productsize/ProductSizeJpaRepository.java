package com.dailyon.productservice.repository.productsize;

import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductSizeJpaRepository extends JpaRepository<ProductSize, Long> {
    boolean existsByCategoryAndNameAndDeletedIsFalse(Category category, String name);
    @Query(value = "SELECT ps " +
            "FROM ProductSize ps " +
            "JOIN FETCH ps.category c " +
            "WHERE c.id = :id AND c.deleted = false AND ps.deleted = false")
    List<ProductSize> findProductSizesByCategoryId(Long id);

    @Query(value = "SELECT ps " +
            "FROM ProductSize ps " +
            "WHERE ps.id IN :productSizeIds AND ps.deleted = false ")
    List<ProductSize> findProductSizesByIds(Set<Long> productSizeIds);

    Optional<ProductSize> findProductSizeByIdAndDeletedIsFalse(Long id);
}
