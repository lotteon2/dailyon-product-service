package com.dailyon.productservice.entity;

import com.dailyon.productservice.entity.ids.ProductStockId;
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
@IdClass(ProductStockId.class)
public class ProductStock implements Comparable<ProductStock> {
    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_size_id")
    private ProductSize productSize;

    @Column(nullable = false)
    private Long quantity;

    public static ProductStock create(Product product, ProductSize productSize, Long quantity) {
        return ProductStock.builder()
                .product(product)
                .productSize(productSize)
                .quantity(quantity)
                .build();
    }

    @Override
    public int compareTo(ProductStock o) {
        int result = this.product.getId().compareTo(o.product.getId());
        if(result == 0) {
            result = this.productSize.getId().compareTo(o.productSize.getId());
        }
        return result;
    }
}
