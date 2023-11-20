package com.dailyon.productservice.dto.response;

import com.dailyon.productservice.entity.Brand;
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
                        .map(brand -> new ReadBrandResponse(brand.getId(), brand.getName()))
                        .collect(Collectors.toList()))
                .build();
    }
}
