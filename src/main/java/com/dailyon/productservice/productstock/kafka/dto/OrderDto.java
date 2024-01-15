package com.dailyon.productservice.productstock.kafka.dto;

import com.dailyon.productservice.productstock.kafka.dto.enums.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private List<ProductInfo> productInfos;
    private List<Long> couponInfos;
    private PaymentInfo paymentInfo;
    private String orderNo;
    private Long memberId;
    private int usedPoints;
    private OrderEvent orderEvent;
    private String orderType;
    private String referralCode;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductInfo implements Comparable<ProductInfo> {
        private Long productId;
        private Long sizeId;
        private Long quantity;

        @Override
        public int compareTo(ProductInfo o) {
            int result = this.productId.compareTo(o.productId);
            if(result == 0) {
                result = this.sizeId.compareTo(o.productId);
            }
            return result;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentInfo {
        private String pgToken;
    }

    public void setOrderEvent(OrderEvent orderEvent) {
        this.orderEvent = orderEvent;
    }
}
