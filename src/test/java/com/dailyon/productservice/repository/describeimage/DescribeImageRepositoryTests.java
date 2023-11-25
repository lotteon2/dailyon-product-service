package com.dailyon.productservice.repository.describeimage;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.describeimage.entity.DescribeImage;
import com.dailyon.productservice.describeimage.repository.DescribeImageRepository;
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
