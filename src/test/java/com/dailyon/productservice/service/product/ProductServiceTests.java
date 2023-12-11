package com.dailyon.productservice.service.product;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.describeimage.entity.DescribeImage;
import com.dailyon.productservice.describeimage.repository.DescribeImageRepository;
import com.dailyon.productservice.product.dto.request.CreateProductRequest;
import com.dailyon.productservice.product.dto.request.ProductStockRequest;
import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.product.dto.response.ReadOOTDSearchSliceResponse;
import com.dailyon.productservice.product.dto.response.ReadProductPageResponse;
import com.dailyon.productservice.product.dto.response.ReadProductSliceResponse;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.repository.ProductRepository;
import com.dailyon.productservice.productsize.entity.ProductSize;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.product.service.ProductService;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.dailyon.productservice.productstock.repository.ProductStockRepository;
import com.dailyon.productservice.reviewaggregate.entity.ReviewAggregate;
import com.dailyon.productservice.reviewaggregate.repository.ReviewAggregateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductStockRepository productStockRepository;

    @Autowired
    DescribeImageRepository describeImageRepository;

    @Autowired
    ReviewAggregateRepository reviewAggregateRepository;

    private Brand brand = null;
    private Category category = null;
    private ProductSize productSize1 = null;
    private ProductSize productSize2 = null;
    private List<ProductStockRequest> productStocks = new ArrayList<>();
    private List<String> describeImages = new ArrayList<>();

    private Product product;

    @BeforeEach
    void before() {
        brand = brandRepository.save(Brand.createBrand("brand"));
        category = categoryRepository.save(Category.createRootCategory("category"));

        productSize1 = productSizeRepository.save(ProductSize.create(category, "productSize1"));
        productSize2 = productSizeRepository.save(ProductSize.create(category, "productSize2"));

        productStocks.add(ProductStockRequest.builder().productSizeId(productSize1.getId()).quantity(10L).build());
        productStocks.add(ProductStockRequest.builder().productSizeId(productSize2.getId()).quantity(20L).build());

        describeImages.add("testDescribeImg.jpg");
        describeImages.add("testDescribeImg1.jpg");

        product = productRepository.save(Product.create(
                brand,
                category,
                ProductType.NORMAL,
                Gender.COMMON,
                "TEST_NAME",
                "TEST_CODE",
                "TEST_IMG_URL",
                1000
                )
        );

        List<ProductStock> createProductStocks = new ArrayList<>();
        createProductStocks.add(ProductStock.create(product, productSize1, 100L));
        createProductStocks.add(ProductStock.create(product, productSize2, 200L));
        productStockRepository.saveAll(createProductStocks);

        List<DescribeImage> createDescribeImages = new ArrayList<>();
        createDescribeImages.add(DescribeImage.create(product, "testDescribeImg.jpg"));
        createDescribeImages.add(DescribeImage.create(product, "testDescribeImg1.jpg"));
        describeImageRepository.saveAll(createDescribeImages);

        reviewAggregateRepository.save(ReviewAggregate.create(product, 0.0F, 0L));

        entityManager.flush();
        entityManager.clear();
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
        productStocks.add(ProductStockRequest.builder().productSizeId(0L).quantity(30L).build());

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

    @Test
    @DisplayName("상품 목록 조회")
    void readProductSlice() {
        // given, when
        ReadProductSliceResponse response = productService.readProductSlice(
                0L,
                null,
                null,
                null,
                ProductType.valueOf("NORMAL")
        );

        // then
        assertFalse(response.isHasNext());
        assertEquals(1, response.getProductResponses().size());
    }

    @Test
    @DisplayName("상품 목록 검색")
    void searchProductsTest() {
        // given, when
        ReadProductSliceResponse response
                = productService.searchProductSlice(0L, "TEST");

        // then
        assertFalse(response.isHasNext());
        assertEquals(1, response.getProductResponses().size());
    }

    @Test
    @DisplayName("OOTD에서 상품 검색")
    void searchProductsFromOOTDTest() {
        // given, when
        ReadOOTDSearchSliceResponse response
                = productService.searchFromOOTD(0L, "TEST");

        // then
        assertFalse(response.isHasNext());
        assertEquals(1, response.getProducts().size());
    }

    @Test
    @DisplayName("상품 목록 조회 - 관리자")
    void readProductPage() {
        // given, when
        ReadProductPageResponse response = productService.readProductPage(
                null, null, ProductType.valueOf("NORMAL"), PageRequest.of(0, 8)
        );

        // then
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(2, response.getProductResponses().get(0).getProductStocks().size());
        assertEquals(2, response.getProductResponses().get(0).getDescribeImgUrls().size());
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteProductsTest() {
        // given
        Long productId = product.getId();
        List<Long> ids = new ArrayList<>();
        ids.add(productId);

        // when
        productService.deleteProductsByIds(ids);

        entityManager.flush();
        entityManager.clear();

        // then
        assertThrows(NoSuchElementException.class, () -> productRepository.findById(productId).get());
    }
}
