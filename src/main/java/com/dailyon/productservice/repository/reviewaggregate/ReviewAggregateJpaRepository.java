package com.dailyon.productservice.repository.reviewaggregate;

import com.dailyon.productservice.entity.Product;
import com.dailyon.productservice.entity.ReviewAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewAggregateJpaRepository extends JpaRepository<ReviewAggregate, Product> {
}
