package com.dailyon.productservice.reviewaggregate.repository;

import com.dailyon.productservice.reviewaggregate.entity.ReviewAggregate;

public interface ReviewAggregateRepository {
    ReviewAggregate save(ReviewAggregate reviewAggregate);
}
