package com.dailyon.productservice.controller.category;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CategoryControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    CategoryService categoryService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("하위 카테고리 목록 조회 성공")
    void readChildrenCategoriesSuccess() throws Exception {
        // given
        Category category = categoryService.createCategory(CreateCategoryRequest.builder().categoryName("name").build());
        for(int i=0; i<3; i++) {
            categoryService.createCategory(CreateCategoryRequest.builder()
                    .masterCategoryId(category.getId())
                    .categoryName("children_"+i)
                    .build());
        }

        // when
        ResultActions resultActions = mockMvc.perform(get("/categories/id/"+category.getId()));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.categoryResponses").isArray());
    }

    @Test
    @DisplayName("하위 카테고리 목록 조회 실패 - 존재하지 않는 id")
    void readChildrenCategoriesFail() throws Exception {
        // given
        long categoryId = 0L;

        // when
        ResultActions resultActions = mockMvc.perform(get("/categories/id/" + categoryId));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("브레드크럼 조회")
    void readBreadCrumb() throws Exception {
        // given
        Category root = categoryService.createCategory(CreateCategoryRequest.builder().categoryName("root").build());

        Category mid = categoryService.createCategory(CreateCategoryRequest.builder().masterCategoryId(root.getId()).categoryName("mid").build());

        Category leaf = categoryService.createCategory(CreateCategoryRequest.builder().masterCategoryId(mid.getId()).categoryName("leaf").build());

        // when
        ResultActions resultActions = mockMvc.perform(get("/categories/breadcrumb/"+leaf.getId()));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.breadCrumbs").isArray());
    }

    @Test
    @DisplayName("최상위 카테고리 목록 조회")
    void readRootCategories() throws Exception {
        // given
        Category root1 = categoryService.createCategory(CreateCategoryRequest.builder().categoryName("root1").build());
        Category root2 = categoryService.createCategory(CreateCategoryRequest.builder().categoryName("root2").build());

        // when
        ResultActions resultActions = mockMvc.perform(get("/categories/master"));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryResponses").isArray());
    }
}
