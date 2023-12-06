package com.dailyon.productservice.productsize.dto.response;

import com.dailyon.productservice.productsize.entity.ProductSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductSizePageResponse {
    private Long totalElements;
    private Integer totalPages;

    List<ReadProductSizeResponse> productSizes;

    public static ReadProductSizePageResponse fromEntity(Page<ProductSize> productSizes) {
        return ReadProductSizePageResponse.builder()
                .totalElements(productSizes.getTotalElements())
                .totalPages(productSizes.getTotalPages())
                .productSizes(productSizes.stream()
                        .map(ReadProductSizeResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
