package com.dailyon.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAggregate {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Float avgRating;

    @Column(nullable = false)
    private Long reviewCount;

    public static ReviewAggregate create(Product product, Float avgRating, Long reviewCount) {
        return ReviewAggregate.builder()
                .product(product)
                .avgRating(avgRating)
                .reviewCount(reviewCount)
                .build();
    }
}
