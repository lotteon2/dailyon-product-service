package com.dailyon.productservice.productstock.kafka;

import com.dailyon.productservice.productstock.kafka.dto.OrderDto;
import com.dailyon.productservice.productstock.service.ProductStockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import dailyon.domain.common.KafkaTopic;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderHandler {
    private final ProductStockService productStockService;
    private final CancelOrderHandler cancelOrderHandler;
    private final CreateOrderProductHandler createOrderProductHandler;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaTopic.CREATE_ORDER)
    public void consume(String message, Acknowledgment ack) {
        OrderDto orderDto = null;
        try {
            orderDto = objectMapper.readValue(message, OrderDto.class);
            productStockService.deductProductStocks(orderDto.getProductInfos());
            createOrderProductHandler.produce(orderDto);
        } catch (Exception e) {
            assert orderDto != null;
            log.error(e.getMessage());
            cancelOrderHandler.produce(orderDto);
        } finally {
            ack.acknowledge();
        }
    }
}
