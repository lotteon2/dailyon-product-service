package com.dailyon.productservice.reviewaggregate.repository;

import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.reviewaggregate.entity.ReviewAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewAggregateJpaRepository extends JpaRepository<ReviewAggregate, Product> {
}
