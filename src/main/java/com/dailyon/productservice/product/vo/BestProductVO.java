package com.dailyon.productservice.product.vo;

import com.dailyon.productservice.product.entity.Product;
import dailyon.domain.order.clients.ProductRankResponse;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BestProductVO implements Serializable, Comparable<BestProductVO> {
    private Long id;
    private String brandName;
    private String categoryName;
    private Integer price;
    private String name;
    private String code;
    private String imgUrl;
    private Long count;

    public static BestProductVO create(Product product, ProductRankResponse rank) {
        return BestProductVO.builder()
                .id(product.getId())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .price(product.getPrice())
                .name(product.getName())
                .code(product.getCode())
                .imgUrl(product.getImgUrl())
                .count(rank.getCount())
                .build();
    }

    @Override
    public int compareTo(BestProductVO o) {
        return o.count.compareTo(this.count);
    }
}
