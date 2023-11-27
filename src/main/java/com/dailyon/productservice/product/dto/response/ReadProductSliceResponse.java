package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductSliceResponse {
    private boolean hasNext;
    private List<ReadProductResponse> productResponses;

    public static ReadProductSliceResponse fromEntity(Slice<Product> products) {
        return ReadProductSliceResponse.builder()
                .hasNext(products.hasNext())
                .productResponses(products.stream()
                        .map(ReadProductResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
