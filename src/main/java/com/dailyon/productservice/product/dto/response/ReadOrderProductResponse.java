package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.productstock.entity.ProductStock;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadOrderProductResponse {
    private Long productId;
    private String productName;
    private Integer price;
    private Gender gender;
    private String imgUrl;

    private Long categoryId;

    private Long sizeId;
    private String sizeName;
    private Long stock;

    public static ReadOrderProductResponse fromEntity(ProductStock productStock) {
        return ReadOrderProductResponse.builder()
                .productId(productStock.getProduct().getId())
                .productName(productStock.getProduct().getName())
                .price(productStock.getProduct().getPrice())
                .gender(productStock.getProduct().getGender())
                .imgUrl(productStock.getProduct().getImgUrl())
                .categoryId(productStock.getProduct().getCategory().getId())
                .sizeId(productStock.getProductSize().getId())
                .sizeName(productStock.getProductSize().getName())
                .stock(productStock.getQuantity())
                .build();
    }
}
