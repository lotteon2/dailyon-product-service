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
public class DescribeImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    public static DescribeImage create(Product product, String imgUrl) {
        return DescribeImage.builder()
                .product(product)
                .imgUrl(imgUrl)
                .build();
    }
}
