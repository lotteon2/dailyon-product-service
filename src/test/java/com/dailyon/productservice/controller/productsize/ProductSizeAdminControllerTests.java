package com.dailyon.productservice.controller.productsize;

import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.dto.response.CreateCategoryResponse;
import com.dailyon.productservice.productsize.dto.request.CreateProductSizeRequest;
import com.dailyon.productservice.productsize.dto.response.CreateProductSizeResponse;
import com.dailyon.productservice.category.service.CategoryService;
import com.dailyon.productservice.productsize.service.ProductSizeService;
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

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    EntityManager entityManager;

    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @DisplayName("치수 등록 성공")
    void createProductSizeSuccess() throws Exception {
        // given
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .categoryName("test")
                .build();

        CreateCategoryResponse category = categoryService.createCategory(createCategoryRequest);

        CreateProductSizeRequest createProductSizeRequest = CreateProductSizeRequest.builder()
                .categoryId(category.getCategoryId())
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
        CreateCategoryResponse category = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("test")
                .build());

        productSizeService.createProductSize(CreateProductSizeRequest.builder()
                .categoryId(category.getCategoryId())
                .name("test1")
                .build());

        CreateProductSizeRequest createProductSizeRequest = CreateProductSizeRequest.builder()
                .categoryId(category.getCategoryId())
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

    @Test
    @DisplayName("치수 목록 조회")
    void readProductSizeList() throws Exception {
        // given
        CreateCategoryResponse categoryForProductList = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("CATEGORY_FOR_LIST")
                .build());

        List<CreateProductSizeResponse> productSizes = new ArrayList<>();
        for(int i=0; i<3; i++) {
            productSizes.add(productSizeService.createProductSize(CreateProductSizeRequest.builder()
                    .categoryId(categoryForProductList.getCategoryId())
                    .name("PRODUCT_SIZE_"+i)
                    .build()));
        }

        entityManager.clear();

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/admin/product-size/"+categoryForProductList.getCategoryId())
                        .header("role", "ADMIN")
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSizes").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSizes.size()").value(3));
    }

    @Test
    @DisplayName("치수 페이징 조회")
    void readProductSizePages() throws Exception {
        // given
        CreateCategoryResponse categoryForProductList = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("CATEGORY_FOR_LIST")
                .build());

        List<CreateProductSizeResponse> productSizes = new ArrayList<>();
        for(int i=0; i<3; i++) {
            productSizes.add(productSizeService.createProductSize(CreateProductSizeRequest.builder()
                    .categoryId(categoryForProductList.getCategoryId())
                    .name("PRODUCT_SIZE_"+i)
                    .build()));
        }

        entityManager.clear();

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/admin/page/product-size/"+categoryForProductList.getCategoryId())
                        .header("role", "ADMIN")
        );

        resultActions
                .andExpect(MockMvcResultMatchers.jsonPath("$.productSizes.size()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));
    }
}
