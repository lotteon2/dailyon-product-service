package com.dailyon.productservice.repository.productsize;

import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSizeJpaRepository extends JpaRepository<ProductSize, Long> {
    boolean existsByCategoryAndName(Category category, String name);
}
