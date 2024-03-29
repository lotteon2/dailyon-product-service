package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.describeimage.entity.DescribeImage;
import com.dailyon.productservice.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductAdminResponse {
    private Long id;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private String name;
    private Gender gender;
    private Integer price;
    private String code;
    private String imgUrl;
    private Double avgRating;
    private Long reviewCount;
    private List<ReadProductStockAdminResponse> productStocks;
    private List<String> describeImgUrls;

    public static ReadProductAdminResponse fromEntity(Product product) {
        return ReadProductAdminResponse.builder()
                .id(product.getId())
                .brandId(product.getBrand().getId())
                .brandName(product.getBrand().getName())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .name(product.getName())
                .gender(product.getGender())
                .price(product.getPrice())
                .code(product.getCode())
                .imgUrl(product.getImgUrl())
                .avgRating(product.getReviewAggregate().getAvgRating())
                .reviewCount(product.getReviewAggregate().getReviewCount())
                .productStocks(product.getProductStocks().stream()
                        .map(ReadProductStockAdminResponse::fromEntity)
                        .collect(Collectors.toList()))
                .describeImgUrls(product.getDescribeImages().stream()
                        .map(DescribeImage::getImgUrl)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
