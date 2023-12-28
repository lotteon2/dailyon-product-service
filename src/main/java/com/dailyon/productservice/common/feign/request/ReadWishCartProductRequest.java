package com.dailyon.productservice.common.feign.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadWishCartProductRequest {
    private Long productId;
    private Long productSizeId;
}
