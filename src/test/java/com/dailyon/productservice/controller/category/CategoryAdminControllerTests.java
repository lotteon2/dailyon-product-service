package com.dailyon.productservice.controller.category;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.dto.request.UpdateCategoryRequest;
import com.dailyon.productservice.category.dto.response.CreateCategoryResponse;
import com.dailyon.productservice.category.service.CategoryService;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class CategoryAdminControllerTests extends IntegrationTestSupport {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    CategoryService categoryService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("카테고리 등록 실패 - 중복 이름")
    void createCategoryFail1() throws Exception {
        // given
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .categoryName("test")
                .build();

        mockMvc.perform(
                post("/admin/categories")
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryRequest))
        );

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/admin/categories")
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryRequest))
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("카테고리 등록 실패 - 존재하지 않는 상위 카테고리")
    void createCategoryFail2() throws Exception {
        // given
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .masterCategoryId(2L)
                .categoryName("test")
                .build();

        String requestBody = objectMapper.writeValueAsString(createCategoryRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/admin/categories")
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("최상위 카테고리 등록 성공")
    void createRootCategorySuccess() throws Exception {
        // given
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .categoryName("test")
                .build();

        String requestBody = objectMapper.writeValueAsString(createCategoryRequest);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/admin/categories")
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("하위 카테고리 등록 성공")
    void createCategorySuccess() throws Exception {
        // given
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .categoryName("test")
                .build();

        CreateCategoryResponse masterCategory = categoryService.createCategory(createCategoryRequest);

        CreateCategoryRequest createCategoryRequest1 = CreateCategoryRequest.builder()
                .masterCategoryId(masterCategory.getCategoryId())
                .categoryName("test1")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/admin/categories")
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(createCategoryRequest1))
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("전체 카테고리 목록 조회")
    void readAllCategories() throws Exception {
        // given
        categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("test")
                .build());

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/admin/categories")
                        .header("role", "ROLE_ADMIN")
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.allCategories").isArray());
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    void updateCategorySuccess() throws Exception {
        // given
        CreateCategoryResponse root = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build()
        );

        UpdateCategoryRequest updateCategoryRequest = UpdateCategoryRequest.builder()
                .name("update")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/admin/categories/"+root.getCategoryId())
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryRequest))
        );

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("카테고리 수정 실패 - Valid")
    void updateCategoryFail() throws Exception {
        // given
        CreateCategoryResponse root = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build()
        );

        UpdateCategoryRequest updateCategoryRequest = UpdateCategoryRequest.builder()
                .name("")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/admin/categories/"+root.getCategoryId())
                        .header("role", "ROLE_ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryRequest))
        );

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("카테고리 페이지 조회")
    void readCategoryPages() throws Exception {
        // given, when
        ResultActions resultActions = mockMvc.perform(
                get("/admin/page/categories")
                        .header("role", "ROLE_ADMIN")
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responses.size()").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(0));
    }

    @Test
    @DisplayName("카테고리 삭제 실패 - 존재하지 않는 카테고리")
    void deleteCategoryFail() throws Exception {
        // given, when
        ResultActions resultActions = mockMvc.perform(
                delete("/admin/categories/"+0)
                        .header("role", "ROLE_ADMIN")
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(NotExistsException.CATEGORY_NOT_FOUND));
    }
}
