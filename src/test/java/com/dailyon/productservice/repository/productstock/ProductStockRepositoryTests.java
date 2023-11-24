package com.dailyon.productservice.repository.productstock;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.entity.*;
import com.dailyon.productservice.enums.Gender;
import com.dailyon.productservice.enums.ProductType;
import com.dailyon.productservice.repository.brand.BrandRepository;
import com.dailyon.productservice.repository.category.CategoryRepository;
import com.dailyon.productservice.repository.product.ProductRepository;
import com.dailyon.productservice.repository.productsize.ProductSizeRepository;
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
public class ProductStockRepositoryTests {
    @Autowired
    ProductStockRepository productStockRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductSizeRepository productSizeRepository;

    private Product product;
    private List<ProductSize> productSizes = new ArrayList<>();

    @BeforeEach
    void before() {
        Brand brand = brandRepository.save(Brand.createBrand("brand"));
        Category category = categoryRepository.save(Category.createRootCategory("root"));

        product = productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                "name", "code", "imgUrl", 1000
        ));

        for(int i=0; i<3; i++) {
            productSizes.add(productSizeRepository.save(ProductSize.create(category, "Product Size_"+i)));
        }
    }

    @Test
    @DisplayName("상품 재고 등록 성공")
    void createProductStockSuccess() {
        // given
        List<ProductStock> productStocks = new ArrayList<>();
        for(ProductSize productSize: productSizes) {
            productStocks.add(ProductStock.create(product, productSize, 10L));
        }

        // when
        List<ProductStock> created = productStockRepository.saveAll(productStocks);

        // then
        assertEquals(productStocks.size(), created.size());
    }
}
