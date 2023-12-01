package com.dailyon.productservice.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductResponse {
    private Long productId;
    private String imgPresignedUrl;
    private Map<String, String> describeImgPresignedUrl;

    public static CreateProductResponse create(Long productId, String imgPresignedUrl, Map<String, String> describeImgPresignedUrl) {
        return CreateProductResponse.builder()
                .productId(productId)
                .imgPresignedUrl(imgPresignedUrl)
                .describeImgPresignedUrl(describeImgPresignedUrl)
                .build();
    }
}
