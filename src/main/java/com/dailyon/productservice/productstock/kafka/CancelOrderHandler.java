package com.dailyon.productservice.productstock.kafka;

import com.dailyon.productservice.productstock.kafka.dto.OrderDto;
import com.dailyon.productservice.productstock.kafka.dto.enums.OrderEvent;
import com.dailyon.productservice.productstock.service.ProductStockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelOrderHandler {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ProductStockService productStockService;
    private final ObjectMapper objectMapper;

    public void produce(OrderDto orderDto) {
        try {
            orderDto.setOrderEvent(OrderEvent.STOCK_FAIL);
            kafkaTemplate.send("cancel-order", objectMapper.writeValueAsString(orderDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "cancel-order")
    public void consume(String message, Acknowledgment ack) {
        try {
            OrderDto orderDto = objectMapper.readValue(message, OrderDto.class);
            if(orderDto.getOrderEvent() != OrderEvent.STOCK_FAIL) {
                productStockService.addProductStocks(orderDto.getProductInfos());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();
        }
    }
}
