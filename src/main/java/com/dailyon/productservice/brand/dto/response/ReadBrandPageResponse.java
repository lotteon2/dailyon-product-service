package com.dailyon.productservice.brand.dto.response;

import com.dailyon.productservice.brand.entity.Brand;
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
public class ReadBrandPageResponse {
    private Long totalElements;
    private Integer totalPages;

    private List<ReadBrandResponse> brandResponses;

    public static ReadBrandPageResponse fromEntity(Page<Brand> brands) {
        return ReadBrandPageResponse.builder()
                .totalElements(brands.getTotalElements())
                .totalPages(brands.getTotalPages())
                .brandResponses(brands.stream()
                        .map(ReadBrandResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
