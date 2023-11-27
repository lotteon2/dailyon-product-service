package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductResponse {
    private Long id;
    private String brandName;
    private String categoryName;
    private String name;
    private String code;
    private String imgUrl;
    private Float avgRating;
    private Long reviewCount;

    public static ReadProductResponse fromEntity(Product product) {
        return ReadProductResponse.builder()
                .id(product.getId())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .name(product.getName())
                .code(product.getCode())
                .imgUrl(product.getImgUrl())
                .avgRating(product.getReviewAggregate().getAvgRating())
                .reviewCount(product.getReviewAggregate().getReviewCount())
                .build();
    }
}
