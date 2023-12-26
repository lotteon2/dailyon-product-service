package com.dailyon.productservice.reviewaggregate.repository;

import com.dailyon.productservice.reviewaggregate.entity.ReviewAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewAggregateRepository extends JpaRepository<ReviewAggregate, Long> {

}
