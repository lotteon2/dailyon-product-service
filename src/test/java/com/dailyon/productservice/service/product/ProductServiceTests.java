package com.dailyon.productservice.service.product;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.dto.request.CreateProductRequest;
import com.dailyon.productservice.dto.request.CreateProductStockRequest;
import com.dailyon.productservice.entity.Brand;
import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.entity.ProductSize;
import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.repository.brand.BrandRepository;
import com.dailyon.productservice.repository.category.CategoryRepository;
import com.dailyon.productservice.repository.productsize.ProductSizeRepository;
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
public class ProductServiceTests {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
    @Autowired
    ProductService productService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductSizeRepository productSizeRepository;

    private Brand brand = null;
    private Category category = null;
    private ProductSize productSize1 = null;
    private ProductSize productSize2 = null;
    private List<CreateProductStockRequest> productStocks = new ArrayList<>();
    private List<String> describeImages = new ArrayList<>();

    @BeforeEach
    void before() {
        brand = brandRepository.save(Brand.createBrand("brand"));
        category = categoryRepository.save(Category.createRootCategory("category"));

        productSize1 = productSizeRepository.save(ProductSize.create(category, "productSize1"));
        productSize2 = productSizeRepository.save(ProductSize.create(category, "productSize2"));

        productStocks.add(CreateProductStockRequest.builder().productSizeId(productSize1.getId()).quantity(10L).build());
        productStocks.add(CreateProductStockRequest.builder().productSizeId(productSize2.getId()).quantity(20L).build());

        describeImages.add("testDescribeImg.jpg");
        describeImages.add("testDescribeImg1.jpg");
    }

    // Mock S3 오류로 인한 주석 처리
//    @Test
//    @DisplayName("상품 생성 성공")
//    void createProductSuccess() {
//        // given
//        CreateProductRequest createProductRequest = CreateProductRequest.builder()
//                .brandId(brand.getId())
//                .categoryId(category.getId())
//                .price(1000)
//                .name("product")
//                .gender("COMMON")
//                .type("NORMAL")
//                .code("TEST")
//                .image("testProductImg.png")
//                .productStocks(productStocks)
//                .describeImages(describeImages)
//                .build();
//
//        // when
//        CreateProductResponse createProductResponse = productService.createProduct(createProductRequest);
//
//        // then
//        assertNotNull(createProductResponse.getImgPresignedUrl());
//        assertEquals(describeImages.size(), createProductResponse.getDescribeImgPresignedUrl().size());
//    }

    @Test
    @DisplayName("상품 생성 실패 - 존재하지 않는 브랜드")
    void createProductFail1() {
        // given
        CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .brandId(0L)
                .categoryId(category.getId())
                .price(1000)
                .name("product")
                .gender("COMMON")
                .type("NORMAL")
                .code("TEST")
                .image("testProductImg.png")
                .productStocks(productStocks)
                .describeImages(describeImages)
                .build();

        // when, then
        assertThrows(NotExistsException.class, () -> productService.createProduct(createProductRequest), NotExistsException.BRAND_NOT_FOUND);
    }

    @Test
    @DisplayName("상품 생성 실패 - 존재하지 않는 카테고리")
    void createProductFail2() {
        // given
        CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .brandId(brand.getId())
                .categoryId(0L)
                .price(1000)
                .name("product")
                .gender("COMMON")
                .type("NORMAL")
                .code("TEST")
                .image("testProductImg.png")
                .productStocks(productStocks)
                .describeImages(describeImages)
                .build();

        // when, then
        assertThrows(NotExistsException.class, () -> productService.createProduct(createProductRequest), NotExistsException.CATEGORY_NOT_FOUND);
    }

    @Test
    @DisplayName("상품 생성 실패 - 존재하지 않는 치수값")
    void createProductFail3() {
        // given
        productStocks.add(CreateProductStockRequest.builder().productSizeId(0L).quantity(30L).build());

        CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .brandId(brand.getId())
                .categoryId(category.getId())
                .price(1000)
                .name("product")
                .gender("COMMON")
                .type("NORMAL")
                .code("TEST")
                .image("testProductImg.png")
                .productStocks(productStocks)
                .describeImages(describeImages)
                .build();

        // when, then
        assertThrows(NotExistsException.class, () -> productService.createProduct(createProductRequest), NotExistsException.PRODUCT_SIZE_NOT_FOUND);
    }

    @Test
    @DisplayName("상품 생성 실패 - 존재하지 않는 성별값")
    void createProductFail4() {
        CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .brandId(brand.getId())
                .categoryId(category.getId())
                .price(1000)
                .name("product")
                .gender("ERROR")
                .type("NORMAL")
                .code("TEST")
                .image("testProductImg.png")
                .productStocks(productStocks)
                .describeImages(describeImages)
                .build();

        // when, then
        assertThrows(NotExistsException.class, () -> productService.createProduct(createProductRequest), NotExistsException.GENDER_NOT_FOUND);
    }

    @Test
    @DisplayName("상품 생성 실패 - 존재하지 않는 상품 유형")
    void createProductFail5() {
        CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .brandId(brand.getId())
                .categoryId(category.getId())
                .price(1000)
                .name("product")
                .gender("COMMON")
                .type("ERROR")
                .code("TEST")
                .image("testProductImg.png")
                .productStocks(productStocks)
                .describeImages(describeImages)
                .build();

        // when, then
        assertThrows(NotExistsException.class, () -> productService.createProduct(createProductRequest), NotExistsException.PRODUCT_TYPE_NOT_FOUND);
    }
}
