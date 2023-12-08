package com.dailyon.productservice.common.feign.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponForProductResponse {
    private Long productId;
    private boolean hasAvailableCoupon;
}
