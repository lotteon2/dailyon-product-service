package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReadOOTDSearchSliceResponse {
    private boolean hasNext;
    private List<ReadOOTDSearchResponse> products;

    public static ReadOOTDSearchSliceResponse fromEntity(Slice<Product> products) {
        return ReadOOTDSearchSliceResponse.builder()
                .hasNext(products.hasNext())
                .products(products.stream()
                        .filter(product -> !product.getProductStocks().isEmpty())
                        .map(ReadOOTDSearchResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
