package com.dailyon.productservice.dto.response;

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
    private String imgPresignedUrl;
    private Map<String, String> describeImgPresignedUrl;

    public static CreateProductResponse create(String imgPresignedUrl, Map<String, String> describeImgPresignedUrl) {
        return CreateProductResponse.builder()
                .imgPresignedUrl(imgPresignedUrl)
                .describeImgPresignedUrl(describeImgPresignedUrl)
                .build();
    }
}
