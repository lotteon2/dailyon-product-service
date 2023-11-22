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
    private ProductSize productSize = null;

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

        productSize = productSizeService.createProductSize(createProductSizeRequest);
    }

    @Test
    @DisplayName("치수 등록 실패 - 중복")
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
    @DisplayName("치수 등록 성공 - 다른 카테고리, 같은 name")
    public void createProductSizeSuccess1() {
        // given
        // beforeEach에 (category, "TEST")로 product_size 생성했음
        Category category1 = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("OTHER")
                .build());

        // when
        ProductSize productSize1 = productSizeService.createProductSize(CreateProductSizeRequest.builder()
                .categoryId(category1.getId())
                .name("TEST")
                .build());

        // then
        assertEquals(productSize1.getName(), productSize.getName());
        assertNotEquals(productSize1.getCategory(), productSize.getCategory());
    }

    @Test
    @DisplayName("치수 등록 성공 - 같은 카테고리, 다른 name")
    public void createProductSizeSuccess2() {
        // given
        // beforeEach에 (category, "TEST")로 product_size 생성했음

        // when
        ProductSize productSize1 = productSizeService.createProductSize(CreateProductSizeRequest.builder()
                .categoryId(category.getId())
                .name("TEST1")
                .build());

        // then
        assertNotEquals(productSize1.getName(), productSize.getName());
        assertEquals(productSize1.getCategory(), productSize.getCategory());
    }
}
