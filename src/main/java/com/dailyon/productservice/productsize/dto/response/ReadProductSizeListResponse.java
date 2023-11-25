package com.dailyon.productservice.productsize.dto.response;

import com.dailyon.productservice.productsize.entity.ProductSize;
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
public class ReadProductSizeListResponse {
    List<ReadProductSizeResponse> productSizes;

    public static ReadProductSizeListResponse fromEntity(List<ProductSize> productSizes) {
        return ReadProductSizeListResponse.builder()
                .productSizes(productSizes.stream()
                        .map(ReadProductSizeResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
