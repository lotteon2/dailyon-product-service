package com.dailyon.productservice.common.feign.response;

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
public class ReadOOTDProductListResponse {
    private List<ReadOOTDProductResponse> productInfos;

    public static ReadOOTDProductListResponse fromEntity(List<Product> products) {
        return ReadOOTDProductListResponse.builder()
                .productInfos(products.stream()
                        .map(ReadOOTDProductResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
