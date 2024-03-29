package com.dailyon.productservice.controller.brand;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.brand.dto.request.CreateBrandRequest;
import com.dailyon.productservice.brand.dto.request.UpdateBrandRequest;
import com.dailyon.productservice.brand.dto.response.CreateBrandResponse;
import com.dailyon.productservice.brand.service.BrandService;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class BrandAdminControllerTests extends IntegrationTestSupport {
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
                        .header("role", "ROLE_ADMIN")
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
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isString());
    }

    @Test
    @DisplayName("브랜드 이름 수정 성공")
    void updateBrandControllerSuccess() throws Exception {
        // given
        CreateBrandResponse createdBrand = brandService.createBrand(CreateBrandRequest.builder().name("test").build());

        UpdateBrandRequest updateBrandRequest = UpdateBrandRequest.builder().name("test1").build();
        String requestBody = objectMapper.writeValueAsString(updateBrandRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/admin/brands/"+createdBrand.getBrandId())
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(brandService.readAllBrands().getBrandResponses().get(0).getName(), "test1");
    }

    @Test
    @DisplayName("브랜드 이름 수정 실패 - 존재하지 않는 id")
    void updateBrandControllerFail1() throws Exception {
        // given
        brandService.createBrand(CreateBrandRequest.builder().name("test").build());

        UpdateBrandRequest updateBrandRequest = UpdateBrandRequest.builder().name("test1").build();
        String requestBody = objectMapper.writeValueAsString(updateBrandRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/admin/brands/"+0)
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("브랜드 이름 수정 실패 - 중복 이름")
    void updateBrandControllerFail2() throws Exception {
        // given
        CreateBrandResponse createBrandResponse =
                brandService.createBrand(CreateBrandRequest.builder().name("test").build());

        brandService.createBrand(CreateBrandRequest.builder().name("test1").build());

        UpdateBrandRequest updateBrandRequest = UpdateBrandRequest.builder().name("test1").build();
        String requestBody = objectMapper.writeValueAsString(updateBrandRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/admin/brands/"+createBrandResponse.getBrandId())
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("브랜드 페이지 조회")
    void readBrandPageTest() throws Exception {
        // given, when
        ResultActions resultActions = mockMvc.perform(
                get("/admin/page/brands")
                        .header("role", "ROLE_ADMIN")
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.brandResponses.size()").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(0));
    }

    @Test
    @DisplayName("브랜드 삭제 실패 - 존재하지 않는 브랜드")
    void deleteBrandFail1() throws Exception {
        // given, when
        ResultActions resultActions = mockMvc.perform(
                delete("/admin/brands/"+0)
                        .header("role", "ROLE_ADMIN")
        );

        resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(NotExistsException.BRAND_NOT_FOUND));
    }
}
