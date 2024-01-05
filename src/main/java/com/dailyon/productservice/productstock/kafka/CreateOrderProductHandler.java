package com.dailyon.productservice.productstock.kafka;

import com.dailyon.productservice.productstock.kafka.dto.OrderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import dailyon.domain.common.KafkaTopic;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderProductHandler {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void produce(OrderDto orderDto) {
        try {
            kafkaTemplate.send(KafkaTopic.CREATE_ORDER_PRODUCT, objectMapper.writeValueAsString(orderDto));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
