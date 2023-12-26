package com.dailyon.productservice.repository.reviewaggregate;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.reviewaggregate.entity.ReviewAggregate;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.product.repository.ProductRepository;
import com.dailyon.productservice.reviewaggregate.repository.ReviewAggregateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
public class ReviewAggregateRepositoryTests {
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
    @DisplayName("리뷰 집계 등록 성공")
    void createReviewAggregateSuccess() {
        ReviewAggregate reviewAggregate =
                reviewAggregateRepository.save(ReviewAggregate.create(product, 0D, 0L));

        assertEquals(product.getId(), reviewAggregate.getId());
    }
}
