package com.dailyon.productservice.common.feign.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    @NotNull
    private Long productId;
    @NotNull
    private Long sizeId;
}
