package com.dailyon.productservice.brand.dto.response;

import com.dailyon.productservice.brand.entity.Brand;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadBrandResponse {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return '{' + "'id':" + this.id + ", 'name': '" + this.name + "'}";
    }

    public static ReadBrandResponse fromEntity(Brand brand) {
        return ReadBrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}
