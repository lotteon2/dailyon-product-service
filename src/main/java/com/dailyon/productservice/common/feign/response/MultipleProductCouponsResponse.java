package com.dailyon.productservice.common.feign.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipleProductCouponsResponse {
    private Map<Long, List<CouponInfoItemResponse>> coupons;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponInfoItemResponse {
        private Long couponInfoId;
        private String appliesToType;
        private Long appliedToId;

        private String discountType;
        private Long discountValue;
        private LocalDateTime endAt;

        private Long minPurchaseAmount;
        private Long maxPurchaseAmount;
    }
}
