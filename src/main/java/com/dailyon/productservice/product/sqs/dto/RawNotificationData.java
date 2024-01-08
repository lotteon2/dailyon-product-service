package com.dailyon.productservice.product.sqs.dto;

import com.dailyon.productservice.product.sqs.dto.enums.NotificationType;
import com.dailyon.productservice.productstock.entity.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawNotificationData {
    private String message;
    private Map<String, String> parameters;
    private NotificationType notificationType; // 알림 유형

    private static Map<String, String> createParameter(ProductStock productStock) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("productId", productStock.getProduct().getId().toString());
        parameters.put("sizeId", productStock.getProductSize().getId().toString());
        parameters.put("productName", productStock.getProduct().getName());
        parameters.put("sizeName", productStock.getProductSize().getName());
        return parameters;
    }

    public static RawNotificationData create(ProductStock productStock, NotificationType notificationType) {
        return RawNotificationData.builder()
                .parameters(createParameter(productStock))
                .notificationType(notificationType)
                .build();
    }
}
