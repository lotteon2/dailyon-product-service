package com.dailyon.productservice.productstock.kafka;

import com.dailyon.productservice.common.exception.InsufficientQuantityException;
import com.dailyon.productservice.productstock.kafka.dto.OrderDto;
import com.dailyon.productservice.productstock.service.ProductStockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderHandler {
    private final ProductStockService productStockService;
    private final CancelOrderHandler cancelOrderHandler;
    private final CreateOrderProductHandler createOrderProductHandler;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "create-order")
    public void consume(String message, Acknowledgment ack) {
        OrderDto orderDto = null;
        try {
            orderDto = objectMapper.readValue(message, OrderDto.class);
            productStockService.deductProductStocks(orderDto.getProductInfos());
            createOrderProductHandler.produce(orderDto);
        } catch (InsufficientQuantityException e) {
            assert orderDto != null;
            cancelOrderHandler.produce(orderDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();
        }
    }
}
