package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.productstock.entity.ProductStock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReadOOTDSearchResponse {
    private Long id;
    private String name;
    private String imgUrl;
    private String brandName;
    private List<String> sizeNames;

    public static ReadOOTDSearchResponse fromEntity(Product product) {
        return ReadOOTDSearchResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .imgUrl(product.getImgUrl())
                .brandName(product.getBrand().getName())
                .sizeNames(product.getProductStocks().stream()
                        .map(productStock -> productStock.getProductSize().getName())
                        .collect(Collectors.toList()))
                .build();
    }
}
