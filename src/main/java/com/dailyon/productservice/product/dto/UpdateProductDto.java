package com.dailyon.productservice.product.dto;

import com.dailyon.productservice.productstock.entity.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDto {
    private String imgPresignedUrl;
    private Map<String, String> describeImgPresignedUrls;
    private List<ProductStock> productStocksToNotify;

    public static UpdateProductDto create(String imgPresignedUrl,
                                          Map<String, String> describeImgPresignedUrls,
                                          List<ProductStock> productStocksToNotify) {
        return UpdateProductDto.builder()
                .imgPresignedUrl(imgPresignedUrl)
                .describeImgPresignedUrls(describeImgPresignedUrls)
                .productStocksToNotify(productStocksToNotify)
                .build();
    }
}
