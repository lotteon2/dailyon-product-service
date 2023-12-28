package com.dailyon.productservice.brand.dto.response;

import com.dailyon.productservice.brand.entity.Brand;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateBrandResponse {
    private Long brandId;

    public static CreateBrandResponse fromEntity(Brand brand) {
        return CreateBrandResponse.builder()
                .brandId(brand.getId())
                .build();
    }
}
