package com.dailyon.productservice.product.sqs.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString(exclude = {"whoToNotify"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SQSNotificationDto {
    List<Long> whoToNotify; // if null, 전체유저에게 발송

    RawNotificationData rawNotificationData;

    public static SQSNotificationDto create(RawNotificationData rawNotificationData) {
        return SQSNotificationDto.builder()
                .rawNotificationData(rawNotificationData)
                .build();
    }
}
