package com.dailyon.productservice.describeimage.entity;

import com.dailyon.productservice.product.entity.Product;
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
public class DescribeImage implements Comparable<DescribeImage> {
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

    @Override
    public int compareTo(DescribeImage o) {
        return this.id.compareTo(o.id);
    }
}
