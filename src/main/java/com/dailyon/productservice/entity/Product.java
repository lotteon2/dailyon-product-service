package com.dailyon.productservice.entity;

import com.dailyon.productservice.entity.common.BaseEntity;
import com.dailyon.productservice.enums.Gender;
import com.dailyon.productservice.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<DescribeImage> describeImages = new TreeSet<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ProductStock> productStocks = new TreeSet<>();

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    private ReviewAggregate reviewAggregate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN default false")
    private boolean deleted;

    public static Product create(Brand brand, Category category, ProductType type, Gender gender, String name,
                                 String code, String imgUrl, Integer price) {
        return Product.builder()
                .brand(brand).category(category).type(type).gender(gender)
                .name(name).code(code).imgUrl(imgUrl).price(price)
                .build();
    }
}
