package com.dailyon.productservice.controller.feign;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.common.feign.request.OrderProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class FeignControllerTests extends IntegrationTestSupport {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("query parameter를 반드시 입력해야 한다")
    void readOOTDProductDetailFail() throws Exception {
        // given, when
        ResultActions resultActions = mockMvc.perform(
                get("/clients/products/post-image")
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("request body의 리스트가 비어선 안된다")
    void readOrderProductsFail() throws Exception {
        // given
        List<OrderProductDto> request = new ArrayList<>();

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/clients/products/orders", objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
