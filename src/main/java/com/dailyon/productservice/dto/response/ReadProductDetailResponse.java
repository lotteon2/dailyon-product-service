package com.dailyon.productservice.dto.response;

import com.dailyon.productservice.entity.DescribeImage;
import com.dailyon.productservice.entity.Product;
import com.dailyon.productservice.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductDetailResponse {
    private String brandName;
    private String name;
    private String imgUrl;
    private Integer price;
    private Gender gender;

    private Float avgRating;
    private Long reviewCount;

    private List<ReadProductStockResponse> productStocks;
    private List<String> describeImgUrls;

    public static ReadProductDetailResponse fromEntity(Product product) {
        return ReadProductDetailResponse.builder()
                .brandName(product.getBrand().getName())
                .name(product.getName())
                .imgUrl(product.getImgUrl())
                .price(product.getPrice())
                .gender(product.getGender())
                .avgRating(product.getReviewAggregate().getAvgRating())
                .reviewCount(product.getReviewAggregate().getReviewCount())
                .productStocks(product.getProductStocks().stream()
                        .map(ReadProductStockResponse::fromEntity)
                        .collect(Collectors.toList()))
                .describeImgUrls(product.getDescribeImages().stream()
                        .map(DescribeImage::getImgUrl)
                        .collect(Collectors.toList()))
                .build();
    }
}
