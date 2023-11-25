package com.dailyon.productservice.productsize.dto.response;

import com.dailyon.productservice.productsize.entity.ProductSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductSizeResponse {
    private Long id;
    private String name;

    public static ReadProductSizeResponse fromEntity(ProductSize productSize) {
        return ReadProductSizeResponse.builder()
                .id(productSize.getId())
                .name(productSize.getName())
                .build();
    }
}
