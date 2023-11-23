package com.dailyon.productservice.controller.productsize;

import com.dailyon.productservice.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.dto.request.CreateProductSizeRequest;
import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.service.category.CategoryService;
import com.dailyon.productservice.service.productsize.ProductSizeService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
public class ProductSizeAdminControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductSizeService productSizeService;

    @Autowired
    CategoryService categoryService;

    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @DisplayName("치수 등록 성공")
    void createProductSizeSuccess() throws Exception {
        // given
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .categoryName("test")
                .build();

        Category category = categoryService.createCategory(createCategoryRequest);

        CreateProductSizeRequest createProductSizeRequest = CreateProductSizeRequest.builder()
                .categoryId(category.getId())
                .name("test")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/admin/product-size")
                        .header("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductSizeRequest))
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("치수 등록 실패 - 존재하지 않는 카테고리")
    void createProductSizeFail1() throws Exception {
        // given
        CreateProductSizeRequest createProductSizeRequest = CreateProductSizeRequest.builder()
                .categoryId(0L)
                .name("test")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/admin/product-size")
                        .header("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductSizeRequest))
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("치수 등록 실패 - 중복 이름")
    void createProductSize() throws Exception {
        // given
        Category category = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("test")
                .build());

        productSizeService.createProductSize(CreateProductSizeRequest.builder()
                .categoryId(category.getId())
                .name("test1")
                .build());

        CreateProductSizeRequest createProductSizeRequest = CreateProductSizeRequest.builder()
                .categoryId(category.getId())
                .name("test1")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/admin/product-size")
                        .header("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductSizeRequest))
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}