package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.product.dto.UpdateProductDto;
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

    public static UpdateProductResponse create(UpdateProductDto updateProductDto) {
        return UpdateProductResponse.builder()
                .imgPresignedUrl(updateProductDto.getImgPresignedUrl())
                .describeImgPresignedUrl(updateProductDto.getDescribeImgPresignedUrls())
                .build();
    }
}
