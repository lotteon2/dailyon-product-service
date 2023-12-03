package com.dailyon.productservice.controller.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
public class ProductFeignControllerTests {
    @Autowired
    MockMvc mockMvc;

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
}
