package com.dailyon.productservice.service.category;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.service.CategoryService;
import com.dailyon.productservice.category.dto.request.UpdateCategoryRequest;
import com.dailyon.productservice.category.dto.response.ReadAllCategoryListResponse;
import com.dailyon.productservice.category.dto.response.ReadBreadCrumbListResponse;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.category.repository.CategoryRepository;
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
public class CategoryServiceTests {
    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntityManager em;

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

        em.clear();

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

        List<Category> childrens = new ArrayList<>();
        for(int i=0; i<3; i++) {
            childrens.add(categoryService.createCategory(CreateCategoryRequest.builder()
                    .masterCategoryId(masterCategory.getId())
                    .categoryName("children_"+i)
                    .build())
            );
        }

        em.clear();

        // when
        ReadChildrenCategoryListResponse response = categoryService.readChildrenCategories(masterCategory.getId());

        // then
        assertEquals(3, response.getCategoryResponses().size());
        assertEquals(childrens.size(), response.getCategoryResponses().size());
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

    @Test
    @DisplayName("breadcrumb 조회 - root > mid > leaf")
    void readBreadCrumbList() {
        // given
        Category root = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build());

        Category mid = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(root.getId())
                .categoryName("mid")
                .build());

        Category leaf = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(mid.getId())
                .categoryName("leaf")
                .build());

        // when
        ReadBreadCrumbListResponse breadCrumbs = categoryService.readBreadCrumbs(leaf.getId());

        // then
        assertEquals(3, breadCrumbs.getBreadCrumbs().size());
        assertEquals("root", breadCrumbs.getBreadCrumbs().get(0).getName());
        assertEquals("mid", breadCrumbs.getBreadCrumbs().get(1).getName());
        assertEquals("leaf", breadCrumbs.getBreadCrumbs().get(2).getName());
    }

    @Test
    @DisplayName("카테고리 이름 변경 성공")
    void updateCategorySuccess() {
        // given
        Category root = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build());

        UpdateCategoryRequest updateCategoryRequest = UpdateCategoryRequest.builder()
                .name("UPDATED")
                .build();

        // when
        categoryService.updateCategoryName(root.getId(), updateCategoryRequest);

        Category updated = categoryRepository.findById(root.getId()).get();

        // then
        assertEquals(updated.getName(), updateCategoryRequest.getName());
    }

    @Test
    @DisplayName("카테고리 이름 변경 실패 - 존재하지 않는 카테고리")
    void updateCategoryFail1() {
        // given
        categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build());

        UpdateCategoryRequest updateCategoryRequest = UpdateCategoryRequest.builder()
                .name("UPDATED")
                .build();

        // when, then
        assertThrows(NotExistsException.class, () ->
                categoryService.updateCategoryName(0L, updateCategoryRequest));
    }

    @Test
    @DisplayName("카테고리 이름 변경 실패 - 중복된 이름")
    void updateCategoryFail2() {
        // given
        Category root = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build());

        UpdateCategoryRequest updateCategoryRequest = UpdateCategoryRequest.builder()
                .name("root")
                .build();

        // when, then
        assertThrows(UniqueException.class, () ->
                categoryService.updateCategoryName(root.getId(), updateCategoryRequest));
    }
}
