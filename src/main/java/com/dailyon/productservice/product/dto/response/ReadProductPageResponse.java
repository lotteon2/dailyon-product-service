package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.product.entity.Product;
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
public class ReadProductPageResponse {
    private Long totalElements;
    private Integer totalPages;
    private List<ReadProductAdminResponse> productResponses;

    public static ReadProductPageResponse fromEntity(Page<Product> products) {
        return ReadProductPageResponse.builder()
                .totalElements(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .productResponses(products.stream()
                        .map(ReadProductAdminResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
