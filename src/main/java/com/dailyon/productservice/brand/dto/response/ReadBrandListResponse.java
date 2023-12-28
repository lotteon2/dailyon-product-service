package com.dailyon.productservice.brand.dto.response;

import com.dailyon.productservice.brand.entity.Brand;
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
public class ReadBrandListResponse {
    private List<ReadBrandResponse> brandResponses;

    public static ReadBrandListResponse fromEntity(List<Brand> brands) {
        return ReadBrandListResponse.builder()
                .brandResponses(brands.stream()
                        .map(ReadBrandResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
