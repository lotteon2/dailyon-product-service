package com.dailyon.productservice.service.reviewaggregate;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.repository.ProductRepository;
import com.dailyon.productservice.reviewaggregate.entity.ReviewAggregate;
import com.dailyon.productservice.reviewaggregate.kafka.dto.ReviewDto;
import com.dailyon.productservice.reviewaggregate.repository.ReviewAggregateRepository;
import com.dailyon.productservice.reviewaggregate.service.ReviewAggregateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReviewAggregateServiceTests extends IntegrationTestSupport {
    @Autowired
    ReviewAggregateService reviewAggregateService;

    @Autowired
    ReviewAggregateRepository reviewAggregateRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private Product product;

    @BeforeEach
    void before() {
        Brand brand = brandRepository.save(Brand.createBrand("brand"));
        Category category = categoryRepository.save(Category.createRootCategory("root"));

        product = productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                "name", "code", "imgUrl", 1000
        ));
    }

    @Test
    @DisplayName("리뷰 집계 수정")
    void updateReviewAggregate() {
        reviewAggregateRepository.save(ReviewAggregate.create(product, 0D, 0L));

        ReviewAggregate reviewAggregate = reviewAggregateService.update(ReviewDto.builder()
                .memberId(1L)
                .orderDetailNo("TEST")
                .point(10)
                .productId(product.getId())
                .ratingAvg(4.0)
                .build()
        );

        Assertions.assertEquals(1, reviewAggregate.getReviewCount());
        Assertions.assertEquals(4.0, reviewAggregate.getAvgRating());
    }
}
