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
    private Long categoryId;
    private String name;
    private Gender gender;
    private Integer price;
    private String code;
    private String imgUrl;
    private List<ReadProductStockAdminResponse> productStocks;
    private List<String> describeImgUrls;

    public static ReadProductAdminResponse fromEntity(Product product) {
        return ReadProductAdminResponse.builder()
                .id(product.getId())
                .brandId(product.getBrand().getId())
                .categoryId(product.getCategory().getId())
                .name(product.getName())
                .gender(product.getGender())
                .price(product.getPrice())
                .code(product.getCode())
                .imgUrl(product.getImgUrl())
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
