package com.dailyon.productservice.dto.response;

import com.dailyon.productservice.entity.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductStockResponse {
    private Long productSizeId;
    private String productSizeName;
    private Long quantity;

    public static ReadProductStockResponse fromEntity(ProductStock productStock) {
        return ReadProductStockResponse.builder()
                .productSizeId(productStock.getProductSize().getId())
                .productSizeName(productStock.getProductSize().getName())
                .quantity(productStock.getQuantity())
                .build();
    }
}
