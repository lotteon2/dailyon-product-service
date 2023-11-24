package com.dailyon.productservice.repository.describeimage;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.entity.Brand;
import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.entity.DescribeImage;
import com.dailyon.productservice.entity.Product;
import com.dailyon.productservice.enums.Gender;
import com.dailyon.productservice.enums.ProductType;
import com.dailyon.productservice.repository.brand.BrandRepository;
import com.dailyon.productservice.repository.category.CategoryRepository;
import com.dailyon.productservice.repository.product.ProductRepository;
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
public class DescribeImageRepositoryTests {
    @Autowired
    DescribeImageRepository describeImageRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private Product product;

    @BeforeEach
    void before() {
        Brand brand = brandRepository.save(Brand.createBrand("brand"));
        Category category = categoryRepository.save(Category.createRootCategory("root"));

        product = productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                "name", "code", "imgUrl", 1000
        ));
    }

    @Test
    @DisplayName("상품 이미지 등록 성공")
    void createDescribeImagesSuccess() {
        List<DescribeImage> describeImages = new ArrayList<>();
        describeImages.add(DescribeImage.create(product, "imgUrl1"));
        describeImages.add(DescribeImage.create(product, "imgUrl2"));

        List<DescribeImage> created = describeImageRepository.saveAll(describeImages);

        assertEquals(created.size(), describeImages.size());
    }
}
