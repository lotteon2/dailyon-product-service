package com.dailyon.productservice.service.productstock;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.common.feign.request.OrderProductDto;
import com.dailyon.productservice.product.dto.request.ProductStockRequest;
import com.dailyon.productservice.common.feign.response.ReadOrderProductListResponse;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.repository.ProductRepository;
import com.dailyon.productservice.productsize.entity.ProductSize;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.dailyon.productservice.productstock.repository.ProductStockRepository;
import com.dailyon.productservice.productstock.service.ProductStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
public class ProductStockServiceTests {
    @Autowired
    ProductStockService productStockService;

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
    }



    @Test
    @DisplayName("주문 시 상품 정보 조회")
    void test1() {
        List<OrderProductDto> request = new ArrayList<>();
        request.add(OrderProductDto.builder().productId(product.getId()).sizeId(productSize1.getId()).build());
        request.add(OrderProductDto.builder().productId(product.getId()).sizeId(productSize2.getId()).build());

        ReadOrderProductListResponse response = productStockService.readOrderProducts(request);

        assertEquals(request.size(), response.getResponse().size());
    }
}
