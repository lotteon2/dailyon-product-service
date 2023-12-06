package com.dailyon.productservice.common.feign.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class OrderProductDto {
    @NotNull
    private Long productId;
    @NotNull
    private Long sizeId;
}
