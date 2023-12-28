package com.dailyon.productservice.common.feign.response;

import com.dailyon.productservice.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadOOTDProductResponse {
    private Long id;
    private String name;
    private String brandName;
    private String imgUrl;
    private Integer price;

    public static ReadOOTDProductResponse fromEntity(Product product) {
        return ReadOOTDProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brandName(product.getBrand().getName())
                .imgUrl(product.getImgUrl())
                .price(product.getPrice())
                .build();
    }
}
