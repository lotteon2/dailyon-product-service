package com.dailyon.productservice.product.sqs;

import com.dailyon.productservice.product.sqs.dto.RawNotificationData;
import com.dailyon.productservice.product.sqs.dto.SQSNotificationDto;
import com.dailyon.productservice.product.sqs.dto.enums.NotificationType;
import com.dailyon.productservice.productstock.entity.ProductStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductRestockHandler {
    private final QueueMessagingTemplate sqsTemplate;
    private final String notificationQueue = "product-restock-notification-queue";

    public void produce(List<ProductStock> productStocksToNotify) {
        for(ProductStock productStock: productStocksToNotify) {
            try {
                Message<String> message = MessageBuilder.withPayload(SQSNotificationDto.create(
                        RawNotificationData.create(productStock, NotificationType.PRODUCT_RESTOCK)
                ).toString()).build();

                sqsTemplate.send(notificationQueue, message);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
