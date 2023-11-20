package com.dailyon.productservice.controller.brand;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.dailyon.productservice.dto.request.CreateBrandRequest;
import com.dailyon.productservice.service.BrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
public class BrandAdminControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BrandService brandService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("브랜드 등록 성공")
    void createBrandControllerSuccess() throws Exception {
        // given
        CreateBrandRequest createBrandRequest = CreateBrandRequest.builder()
                .name("test")
                .build();

        String requestBody = objectMapper.writeValueAsString(createBrandRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/admin/brands")
                        .header("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.brandId").isNumber());
    }

    @Test
    @DisplayName("브랜드 등록 실패 - 중복 이름")
    void createBrandControllerFail() throws Exception {
        // given
        CreateBrandRequest createBrandRequest = CreateBrandRequest.builder()
                .name("test")
                .build();

        brandService.createBrand(createBrandRequest); // 하나 이미 생성

        String requestBody = objectMapper.writeValueAsString(createBrandRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/admin/brands")
                        .header("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isString());
    }
}
