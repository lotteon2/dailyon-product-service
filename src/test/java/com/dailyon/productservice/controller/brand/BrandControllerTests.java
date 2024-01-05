package com.dailyon.productservice.controller.brand;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.brand.dto.request.CreateBrandRequest;
import com.dailyon.productservice.brand.service.BrandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class BrandControllerTests extends IntegrationTestSupport {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BrandService brandService;

    @Test
    @DisplayName("브랜드 전체 조회")
    void readAllBrands() throws Exception {
        // given
        brandService.createBrand(CreateBrandRequest.builder().name("test").build());

        // when
        ResultActions resultActions = mockMvc.perform(get("/brands"));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.brandResponses").isArray());
    }
}
