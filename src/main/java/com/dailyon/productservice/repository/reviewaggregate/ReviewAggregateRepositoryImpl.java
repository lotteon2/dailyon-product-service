package com.dailyon.productservice.repository.reviewaggregate;

import com.dailyon.productservice.entity.ReviewAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewAggregateRepositoryImpl implements ReviewAggregateRepository {
    private final ReviewAggregateJpaRepository reviewAggregateJpaRepository;

    @Override
    public ReviewAggregate save(ReviewAggregate reviewAggregate) {
        return reviewAggregateJpaRepository.save(reviewAggregate);
    }
}
