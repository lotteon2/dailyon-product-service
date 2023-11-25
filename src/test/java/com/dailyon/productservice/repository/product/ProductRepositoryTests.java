package com.dailyon.productservice.repository.product;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
public class ProductRepositoryTests {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Brand brand = null;
    Category category = null;

    @BeforeEach
    void before() {
        brand = brandRepository.save(Brand.createBrand("brand"));
        category = categoryRepository.save(Category.createRootCategory("root"));
    }

    @Test
    @DisplayName("상품 등록 성공")
    void createProductSuccess() {
        // given
        String name = "name";
        String code = "TEST";
        String imgUrl = "imgUrl";
        Integer price = 1000;
        // when
        Product product = productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                name, code, imgUrl, price
        ));

        // then
        assertEquals(name, product.getName());
        assertEquals(code, product.getCode());
        assertEquals(imgUrl, product.getImgUrl());
        assertEquals(price, product.getPrice());
        assertEquals(ProductType.NORMAL, product.getType());
    }
}
