package com.dailyon.productservice.service.productsize;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.dto.response.CreateCategoryResponse;
import com.dailyon.productservice.category.service.CategoryService;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.productsize.dto.request.CreateProductSizeRequest;
import com.dailyon.productservice.productsize.dto.request.UpdateProductSizeRequest;
import com.dailyon.productservice.productsize.dto.response.CreateProductSizeResponse;
import com.dailyon.productservice.productsize.dto.response.ReadProductSizeListResponse;
import com.dailyon.productservice.productsize.dto.response.ReadProductSizePageResponse;
import com.dailyon.productservice.productsize.entity.ProductSize;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
import com.dailyon.productservice.productsize.service.ProductSizeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductSizeServiceTests extends IntegrationTestSupport {
    @Autowired
    ProductSizeService productSizeService;

    @Autowired
    ProductSizeRepository productSizeRepository;

    @Autowired
    CategoryService categoryService;

    @Test
    @DisplayName("치수 등록 실패 - 중복")
    public void createProductSizeFail() {
        // given
        String name = "TEST";

        CreateCategoryResponse response = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName(name)
                .build());

        CreateProductSizeRequest createProductSizeRequest = CreateProductSizeRequest.builder()
                .categoryId(response.getCategoryId())
                .name(name)
                .build();

        productSizeService.createProductSize(createProductSizeRequest);

        // when, then
        assertThrows(UniqueException.class, () -> productSizeService.createProductSize(createProductSizeRequest));
    }

    @Test
    @DisplayName("치수 등록 실패 - 존재하지 않는 카테고리 id")
    public void createProductSizeFail2() {
        // given
        String name = "TEST";

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
        CreateCategoryResponse response = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("ROOT")
                .build());

        CreateCategoryResponse another = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("ANOTHER")
                .build());

        // when
        productSizeService.createProductSize(CreateProductSizeRequest.builder()
                .categoryId(response.getCategoryId())
                .name("PRODUCT_SIZE")
                .build());

        // then
        productSizeService.createProductSize(CreateProductSizeRequest.builder()
                        .categoryId(another.getCategoryId())
                        .name("PRODUCT_SIZE")
                        .build());
    }

    @Test
    @DisplayName("치수 등록 성공 - 같은 카테고리, 다른 name")
    public void createProductSizeSuccess2() {
        // given
        CreateCategoryResponse response = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("ROOT")
                .build());

        // when
        productSizeService.createProductSize(CreateProductSizeRequest.builder()
                .categoryId(response.getCategoryId())
                .name("PRODUCT_SIZE")
                .build());

        // then
        productSizeService.createProductSize(CreateProductSizeRequest.builder()
                .categoryId(response.getCategoryId())
                .name("ANOTHER_NAME")
                .build());
    }

    @Test
    @DisplayName("카테고리에 해당하는 치수 목록 조회")
    public void readProductSizeList() {
        // given
        CreateCategoryResponse category = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("ROOT")
                .build());

        List<CreateProductSizeResponse> productSizes = new ArrayList<>();
        for(int i=0; i<3; i++) {
            productSizes.add(productSizeService.createProductSize(CreateProductSizeRequest.builder()
                    .categoryId(category.getCategoryId())
                    .name("PRODUCT_SIZE_"+i)
                    .build()));
        }

        // when
        ReadProductSizeListResponse productSizeList = productSizeService.readProductSizeListByCategory(category.getCategoryId());

        // then
        assertEquals(productSizeList.getProductSizes().size(), productSizes.size());
    }

    @Test
    @DisplayName("치수 수정 성공")
    void updateProductSizeSuccess() {
        // given
        CreateCategoryResponse category = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("ROOT")
                .build());

        CreateProductSizeResponse productSize = productSizeService.createProductSize(CreateProductSizeRequest.builder()
                .categoryId(category.getCategoryId())
                .name("PRODUCT_SIZE")
                .build());

        // when
        String updateName = "UPDATED";
        UpdateProductSizeRequest updateProductSizeRequest = UpdateProductSizeRequest.builder()
                .name(updateName)
                .build();

        productSizeService.updateProductSizeName(productSize.getProductSizeId(), updateProductSizeRequest);

        // then
        ProductSize updated = productSizeRepository.findProductSizeById(productSize.getProductSizeId()).get();
        assertEquals(updateName, updated.getName());
    }

    @Test
    @DisplayName("치수 수정 실패 - 존재하지 않는 치수")
    void updateProductSizeFail1() {
        // given
        String updateName = "UPDATED";
        UpdateProductSizeRequest updateProductSizeRequest = UpdateProductSizeRequest.builder()
                .name(updateName)
                .build();

        // when, then
        assertThrows(NotExistsException.class, () ->
                productSizeService.updateProductSizeName(0L, updateProductSizeRequest));
    }

    @Test
    @DisplayName("치수 수정 실패 - 중복 이름")
    void updateProductSizeFail2() {
        // given
        CreateCategoryResponse response = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("ROOT")
                .build());

        CreateProductSizeResponse productSize = productSizeService.createProductSize(CreateProductSizeRequest.builder()
                .categoryId(response.getCategoryId())
                .name("PRODUCT_SIZE")
                .build());

        CreateProductSizeResponse toUpdate = productSizeService.createProductSize(CreateProductSizeRequest.builder()
                .categoryId(response.getCategoryId())
                .name("ANOTHER")
                .build());

        UpdateProductSizeRequest updateProductSizeRequest = UpdateProductSizeRequest.builder().name("PRODUCT_SIZE").build();

        // when, then
        assertThrows(UniqueException.class, () ->
                productSizeService.updateProductSizeName(toUpdate.getProductSizeId(), updateProductSizeRequest));
    }

    @Test
    @DisplayName("카테고리에 해당하는 치수 페이징 조회")
    public void readProductSizePages() {
        // given
        CreateCategoryResponse response = categoryService.createCategory(CreateCategoryRequest.builder()
                .masterCategoryId(null)
                .categoryName("ROOT")
                .build());

        for(int i=0; i<3; i++) {
            productSizeService.createProductSize(CreateProductSizeRequest.builder()
                    .categoryId(response.getCategoryId())
                    .name("PRODUCT_SIZE_" + i)
                    .build());
        }

        // when
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "updatedAt");
        ReadProductSizePageResponse result =
                productSizeService.readProductSizePage(response.getCategoryId(), pageable);

        // then
        assertEquals(3, result.getProductSizes().size());
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals("PRODUCT_SIZE_2", result.getProductSizes().get(0).getName());
    }
}
