package com.dailyon.productservice.service.productsize;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.dto.request.CreateProductSizeRequest;
import com.dailyon.productservice.dto.response.ReadProductSizeListResponse;
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
import java.util.ArrayList;
import java.util.List;

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

    private Category categoryForProductList = null;
    private List<ProductSize> productSizes = new ArrayList<>();

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

        categoryForProductList = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("CATEGORY_FOR_LIST")
                .build());

        for(int i=0; i<3; i++) {
            productSizes.add(productSizeService.createProductSize(CreateProductSizeRequest.builder()
                    .categoryId(categoryForProductList.getId())
                    .name("PRODUCT_SIZE_"+i)
                    .build()));
        }

        entityManager.clear();
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
        assertNotEquals(productSize1.getCategory().getId(), productSize.getCategory().getId());
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
        assertEquals(productSize1.getCategory().getId(), productSize.getCategory().getId());
    }

    @Test
    @DisplayName("카테고리에 해당하는 치수 목록 조회")
    public void readProductSizeList() {
        // given, when
        ReadProductSizeListResponse productSizeList = productSizeService.readProductSizeListByCategory(categoryForProductList.getId());

        // then
        assertEquals(productSizeList.getProductSizes().size(), productSizes.size());
    }
}
