package com.dailyon.productservice.brand.dto.response;

import com.dailyon.productservice.brand.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadBrandResponse {
    private Long id;
    private String name;

    public static ReadBrandResponse fromEntity(Brand brand) {
        return ReadBrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}
