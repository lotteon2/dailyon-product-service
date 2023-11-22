package com.dailyon.productservice.service.productsize;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.dto.request.CreateProductSizeRequest;
import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.entity.ProductSize;
import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.exception.UniqueException;
import com.dailyon.productservice.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
public class ProductSizeServiceTests {
    @Autowired
    ProductSizeService productSizeService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    EntityManager entityManager;

    private Category category = null;

    @BeforeEach
    void before() {
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("root")
                .build();

        category = categoryService.createCategory(createCategoryRequest);

        CreateProductSizeRequest createProductSizeRequest = CreateProductSizeRequest.builder()
                .categoryId(category.getId())
                .name("TEST")
                .build();

        productSizeService.createProductSize(createProductSizeRequest);
    }

    @Test
    @DisplayName("치수 등록 실패 - 중복 이름")
    public void createProductSizeFail() {
        // given
        String name = "TEST";

        CreateProductSizeRequest createProductSizeRequest = CreateProductSizeRequest.builder()
                .categoryId(category.getId())
                .name(name)
                .build();

        // when, then
        assertThrows(UniqueException.class, () -> productSizeService.createProductSize(createProductSizeRequest));
    }

    @Test
    @DisplayName("치수 등록 실패 - 존재하지 않는 카테고리 id")
    public void createProductSizeFail2() {
        // given
        String name = "TEST1";

        CreateProductSizeRequest createProductSizeRequest = CreateProductSizeRequest.builder()
                .categoryId(0L)
                .name(name)
                .build();

        // when, then
        assertThrows(NotExistsException.class, () -> productSizeService.createProductSize(createProductSizeRequest));
    }

    @Test
    @DisplayName("치수 등록 성공")
    public void createProductSizeSuccess() {
        // given
        String name = "TEST1";
        CreateProductSizeRequest createProductSizeRequest = CreateProductSizeRequest.builder()
                .categoryId(category.getId())
                .name(name)
                .build();

        // when
        ProductSize productSize = productSizeService.createProductSize(createProductSizeRequest);

        // then
        assertEquals(name, productSize.getName());
        assertEquals(category.getId(), productSize.getCategory().getId());
    }
}
