package com.dailyon.productservice.productstock.kafka;

import com.dailyon.productservice.productstock.kafka.dto.OrderDto;
import com.dailyon.productservice.productstock.kafka.dto.RefundDto;
import com.dailyon.productservice.productstock.service.ProductStockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import dailyon.domain.common.KafkaTopic;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateRefundHandler {
    private final ProductStockService productStockService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "create-refund"/*KafkaTopic.CREATE_REFUND*/)
    public void consume(String message, Acknowledgment ack) {
        try {
            RefundDto refundDto = objectMapper.readValue(message, RefundDto.class);

            OrderDto.ProductInfo productInfo = OrderDto.ProductInfo.builder()
                    .productId(refundDto.getProductInfo().getProductId())
                    .sizeId(refundDto.getProductInfo().getSizeId())
                    .quantity(refundDto.getProductInfo().getQuantity())
                    .build();

            productStockService.addProductStocks(List.of(productInfo));
            ack.acknowledge();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
