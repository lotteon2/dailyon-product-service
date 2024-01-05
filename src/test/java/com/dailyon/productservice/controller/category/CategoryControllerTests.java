package com.dailyon.productservice.controller.category;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.dto.response.CreateCategoryResponse;
import com.dailyon.productservice.category.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class CategoryControllerTests extends IntegrationTestSupport {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    CategoryService categoryService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("브레드크럼 조회")
    void readBreadCrumb() throws Exception {
        // given
        CreateCategoryResponse root = categoryService.createCategory(CreateCategoryRequest.builder().categoryName("root").build());
        CreateCategoryResponse mid = categoryService.createCategory(CreateCategoryRequest.builder().masterCategoryId(root.getCategoryId()).categoryName("mid").build());
        CreateCategoryResponse leaf = categoryService.createCategory(CreateCategoryRequest.builder().masterCategoryId(mid.getCategoryId()).categoryName("leaf").build());

        // when
        ResultActions resultActions = mockMvc.perform(get("/categories/breadcrumb/"+leaf.getCategoryId()));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.breadCrumbs").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.breadCrumbs[0].name").value("root"));
    }

    @Test
    @DisplayName("최상위 카테고리 목록 조회")
    void readRootCategories() throws Exception {
        // given
        categoryService.createCategory(CreateCategoryRequest.builder().categoryName("root1").build());
        categoryService.createCategory(CreateCategoryRequest.builder().categoryName("root2").build());

        // when
        ResultActions resultActions = mockMvc.perform(get("/categories/master"));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryResponses").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryResponses.size()").value(2));
    }
}
