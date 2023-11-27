package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.productstock.entity.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductStockAdminResponse {
    private Long productSizeId;
    private Long quantity;

    public static ReadProductStockAdminResponse fromEntity(ProductStock productStock) {
        return ReadProductStockAdminResponse.builder()
                .productSizeId(productStock.getProductSize().getId())
                .quantity(productStock.getQuantity())
                .build();
    }
}
