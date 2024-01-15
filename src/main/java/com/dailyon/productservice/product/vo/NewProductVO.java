package com.dailyon.productservice.product.vo;

import com.dailyon.productservice.product.entity.Product;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NewProductVO implements Serializable {
    private Long id;
    private String brandName;
    private String categoryName;
    private Integer price;
    private String name;
    private String code;
    private String imgUrl;

    public static NewProductVO fromEntity(Product product) {
        return NewProductVO.builder()
                .id(product.getId())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .price(product.getPrice())
                .name(product.getName())
                .code(product.getCode())
                .imgUrl(product.getImgUrl())
                .build();
    }
}
