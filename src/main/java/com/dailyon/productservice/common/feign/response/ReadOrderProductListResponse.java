package com.dailyon.productservice.common.feign.response;
import com.dailyon.productservice.productstock.entity.ProductStock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReadOrderProductListResponse {
    List<ReadOrderProductResponse> response;

    public static ReadOrderProductListResponse fromEntity(List<ProductStock> productStocks) {
        return ReadOrderProductListResponse.builder()
                .response(productStocks.stream()
                        .map(ReadOrderProductResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
