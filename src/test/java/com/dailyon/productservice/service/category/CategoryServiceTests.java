package com.dailyon.productservice.service.category;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.dto.response.ReadAllCategoryListResponse;
import com.dailyon.productservice.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.exception.UniqueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
public class CategoryServiceTests {
    @Autowired
    CategoryService categoryService;

    @Test
    @DisplayName("카테고리 등록 실패 - 중복 이름")
    void createCategoryFail1() {
        // given
        String test = "name";
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .categoryName(test)
                .build();
        categoryService.createCategory(createCategoryRequest);

        // when
        CreateCategoryRequest createCategoryRequest1 = CreateCategoryRequest.builder()
                .categoryName(test)
                .build();

        // then
        assertThrows(UniqueException.class, () -> categoryService.createCategory(createCategoryRequest1));
    }

    @Test
    @DisplayName("카테고리 등록 실패 - 존재하지 않는 상위 카테고리")
    void createCategoryFail2() {
        // given
        String test = "name";
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .masterCategoryId(2L)
                .categoryName(test)
                .build();

        // when, then
        assertThrows(NotExistsException.class, () -> categoryService.createCategory(createCategoryRequest));
    }

    @Test
    @DisplayName("최상위 카테고리 등록 성공")
    void createRootCategorySuccess() {
        // given
        String test = "name";
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .categoryName(test)
                .build();

        // when
        Category category = categoryService.createCategory(createCategoryRequest);

        // then
        assertNull(category.getMasterCategory());
        assertNotNull(category.getId());
    }

    @Test
    @DisplayName("하위 카테고리 등록 성공")
    void createCategorySuccess() {
        // given
        String test = "name";
        CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName(test)
                .build();

        String test1 = "name1";
        Category masterCategory = categoryService.createCategory(createCategoryRequest);

        // when
        CreateCategoryRequest createCategoryRequest1 = CreateCategoryRequest.builder()
                .masterCategoryId(masterCategory.getId())
                .categoryName(test1)
                .build();

        Category category = categoryService.createCategory(createCategoryRequest1);

        // then
        assertEquals(category.getMasterCategory().getId(), masterCategory.getId());
    }

    @Test
    @DisplayName("최상위 카테고리의 하위 카테고리 목록 조회")
    public void readChildrenCategoriesFromRoot() {
        // given
        Category masterCategory = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("master")
                .build());

        for(int i=0; i<3; i++) {
            categoryService.createCategory(CreateCategoryRequest.builder()
                    .masterCategoryId(masterCategory.getId())
                    .categoryName("children_"+i)
                    .build());
        }

        // when
        ReadChildrenCategoryListResponse response = categoryService.readChildrenCategories(masterCategory.getId());

        // then
        assertEquals(response.getCategoryResponses().size(), 3);
    }

    @Test
    @DisplayName("최하위 카테고리의 하위 카테고리 목록 조회")
    public void readChildrenCategoriesFromLeaf() {
        // given
        Category leafCategory = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("leaf")
                .build());

        // when
        ReadChildrenCategoryListResponse response = categoryService.readChildrenCategories(leafCategory.getId());

        // then
        assertEquals(response.getCategoryResponses().size(), 0);
    }

    @Test
    @DisplayName("카테고리 목록 조회 실패 - 존재하지 않는 id")
    public void readChildrenCategoriesFail() {
        // given
        Long id = 0L;

        // when, then
        assertThrows(NotExistsException.class, () -> categoryService.readChildrenCategories(id));
    }

    @Test
    @DisplayName("전체 카테고리 조회")
    public void readAllCategories() {
        // given
        categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("test")
                .build());

        // when
        ReadAllCategoryListResponse response = categoryService.readAllCategories();

        // then
        assertEquals(1, response.getAllCategories().size());
    }
}
