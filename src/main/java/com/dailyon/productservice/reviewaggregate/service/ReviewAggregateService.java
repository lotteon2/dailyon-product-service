package com.dailyon.productservice.reviewaggregate.service;

import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.reviewaggregate.entity.ReviewAggregate;
import com.dailyon.productservice.reviewaggregate.kafka.dto.ReviewDto;
import com.dailyon.productservice.reviewaggregate.repository.ReviewAggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewAggregateService {
    private final ReviewAggregateRepository reviewAggregateRepository;

    @Transactional
    public ReviewAggregate update(ReviewDto reviewDto) {
        ReviewAggregate reviewAggregate = reviewAggregateRepository.findById(reviewDto.getProductId())
                .orElseThrow(() -> new NotExistsException(NotExistsException.PRODUCT_NOT_FOUND));

        reviewAggregate.plusReviewCount();
        reviewAggregate.setAvgRating(reviewDto.getRatingAvg());

        return reviewAggregate;
    }
}
