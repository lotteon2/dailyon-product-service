package com.dailyon.productservice.productsize.dto.response;

import com.dailyon.productservice.productsize.entity.ProductSize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductSizeResponse {
    Long productSizeId;

    public static CreateProductSizeResponse fromEntity(ProductSize productSize) {
        return CreateProductSizeResponse.builder()
                .productSizeId(productSize.getId())
                .build();
    }
}
