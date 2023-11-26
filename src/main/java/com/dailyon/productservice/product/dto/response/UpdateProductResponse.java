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
public class UpdateProductResponse {
    private String imgPresignedUrl;
    private Map<String, String> describeImgPresignedUrl;

    public static UpdateProductResponse create(String imgPresignedUrl, Map<String, String> describeImgPresignedUrl) {
        return UpdateProductResponse.builder()
                .imgPresignedUrl(imgPresignedUrl)
                .describeImgPresignedUrl(describeImgPresignedUrl)
                .build();
    }
}
