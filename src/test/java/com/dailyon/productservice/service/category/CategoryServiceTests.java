package com.dailyon.productservice.service.category;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.dto.response.CreateCategoryResponse;
import com.dailyon.productservice.category.service.CategoryService;
import com.dailyon.productservice.category.dto.request.UpdateCategoryRequest;
import com.dailyon.productservice.category.dto.response.ReadAllCategoryListResponse;
import com.dailyon.productservice.category.dto.response.ReadBreadCrumbListResponse;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.common.exception.DeleteException;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.repository.ProductRepository;
import com.dailyon.productservice.productsize.entity.ProductSize;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
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
public class CategoryServiceTests {
    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    ProductSizeRepository productSizeRepository;

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
        CreateCategoryResponse category = categoryService.createCategory(createCategoryRequest);

        // then
        assertNotNull(category.getCategoryId());
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
        CreateCategoryResponse masterCategory = categoryService.createCategory(createCategoryRequest);

        // when
        String test1 = "name1";
        CreateCategoryRequest createCategoryRequest1 = CreateCategoryRequest.builder()
                .masterCategoryId(masterCategory.getCategoryId())
                .categoryName(test1)
                .build();
        CreateCategoryResponse category = categoryService.createCategory(createCategoryRequest1);

        em.clear();

        // then
        assertEquals(
                categoryRepository.findById(category.getCategoryId()).get().getMasterCategory().getId(),
                categoryRepository.findById(masterCategory.getCategoryId()).get().getId()
        );
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
        CreateCategoryResponse root = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build());

        CreateCategoryResponse mid = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(root.getCategoryId())
                .categoryName("mid")
                .build());

        CreateCategoryResponse leaf = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(mid.getCategoryId())
                .categoryName("leaf")
                .build());

        // when
        ReadBreadCrumbListResponse breadCrumbs = categoryService.readBreadCrumbs(leaf.getCategoryId());

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
        CreateCategoryResponse root = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build());

        UpdateCategoryRequest updateCategoryRequest = UpdateCategoryRequest.builder()
                .name("UPDATED")
                .build();

        // when
        categoryService.updateCategoryName(root.getCategoryId(), updateCategoryRequest);

        Category updated = categoryRepository.findById(root.getCategoryId()).get();

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
        CreateCategoryResponse root = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build());

        UpdateCategoryRequest updateCategoryRequest = UpdateCategoryRequest.builder()
                .name("root")
                .build();

        // when, then
        assertThrows(UniqueException.class, () ->
                categoryService.updateCategoryName(root.getCategoryId(), updateCategoryRequest));
    }

    @Test
    @DisplayName("최상위 카테고리 목록 조회")
    void readRootCategories() {
        // given
        categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build());

        categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root1")
                .build());

        categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root2")
                .build());

        // when
        ReadChildrenCategoryListResponse response = categoryService.readChildrenCategoriesByMaster(null);

        // then
        assertEquals(3L, response.getCategoryResponses().size());
    }

    @Test
    @DisplayName("하위 카테고리 목록 조회")
    void readChildrenCategories() {
        // given
        CreateCategoryResponse response = categoryService.createCategory(CreateCategoryRequest.builder()
                .categoryName("root")
                .build());

        categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(response.getCategoryId())
                .categoryName("child1")
                .build());

        categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(response.getCategoryId())
                .categoryName("child2")
                .build());

        // when
        ReadChildrenCategoryListResponse result = categoryService.readChildrenCategoriesByMaster(response.getCategoryId());

        // then
        assertEquals(2, result.getCategoryResponses().size());
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategorySuccess() {
        // given
        Category category = categoryRepository.save(Category.createRootCategory("root"));
        productSizeRepository.save(ProductSize.create(category, "PS_1"));

        // when
        Long categoryId = category.getId();
        categoryService.deleteCategory(categoryId);

        // then
        assertEquals(0, categoryService.readAllCategories().getAllCategories().size());
        assertEquals(0, productSizeRepository.findProductSizesByCategoryId(categoryId).size());
    }

    @Test
    @DisplayName("카테고리 삭제 실패 - 상품 존재")
    void deleteCategoryFail1() {
        // given
        Category category = categoryRepository.save(Category.createRootCategory("root"));
        Brand brand = brandRepository.save(Brand.createBrand("test"));

        productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                "name", "code", "imgUrl", 1000
        ));

        // when, then
        assertThrows(DeleteException.class, () -> categoryService.deleteCategory(category.getId()));
    }

    @Test
    @DisplayName("카테고리 삭제 실패 - 존재하지 않는 카테고리")
    void deleteCategoryFail2() {
        // given
        Long wrongCategoryId = 0L;

        // when, then
        assertThrows(NotExistsException.class, () -> categoryService.deleteCategory(wrongCategoryId));
    }
}
