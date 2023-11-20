package com.dailyon.productservice.controller.brand;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.dailyon.productservice.dto.request.CreateBrandRequest;
import com.dailyon.productservice.service.BrandService;
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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
public class BrandControllerTests {
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
