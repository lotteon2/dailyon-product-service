package com.dailyon.productservice.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockRequest implements Comparable<ProductStockRequest> {
    @NotNull(message = "치수값을 입력해주세요")
    private Long productSizeId;

    @Min(value = 1, message = "1개 이상이어야 합니다")
    @Max(value = Long.MAX_VALUE, message = "개수 범위를 초과했습니다")
    private Long quantity;

    @Override
    public int compareTo(ProductStockRequest o) {
        return this.productSizeId.compareTo(o.productSizeId);
    }
}
