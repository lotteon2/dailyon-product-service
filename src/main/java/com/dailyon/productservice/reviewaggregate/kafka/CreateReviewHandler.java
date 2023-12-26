package com.dailyon.productservice.reviewaggregate.kafka;

import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.reviewaggregate.kafka.dto.ReviewDto;
import com.dailyon.productservice.reviewaggregate.service.ReviewAggregateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateReviewHandler {
    private final ReviewAggregateService reviewAggregateService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "create-review")
    public void consume(String message, Acknowledgment ack) {
        ReviewDto reviewDto;
        try {
            reviewDto = objectMapper.readValue(message, ReviewDto.class);
            reviewAggregateService.update(reviewDto);
        } catch (JsonProcessingException | NotExistsException e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();
        }
    }
}
