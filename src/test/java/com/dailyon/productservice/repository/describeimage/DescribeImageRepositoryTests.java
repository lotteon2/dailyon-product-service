package com.dailyon.productservice.repository.describeimage;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.describeimage.entity.DescribeImage;
import com.dailyon.productservice.describeimage.repository.DescribeImageRepository;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DescribeImageRepositoryTests extends IntegrationTestSupport {
    @Autowired
    DescribeImageRepository describeImageRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntityManager entityManager;

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

    @Test
    @DisplayName("상품 이미지 목록 삭제 성공")
    void deleteDescribeImageSuccess() {
        // given
        List<DescribeImage> describeImages = new ArrayList<>();
        describeImages.add(DescribeImage.create(product, "imgUrl1"));
        describeImages.add(DescribeImage.create(product, "imgUrl2"));
        describeImageRepository.saveAll(describeImages);

        // when
        describeImageRepository.deleteByProductId(product.getId());

        // then
        assertEquals(0, describeImageRepository.findAll().size());
    }

    @Test
    @DisplayName("상품 상세 이미지 없는 상품 조회 성공")
    void readProductDetailWithoutDescribeImg() {
        entityManager.flush();
        entityManager.clear();

        Optional<Product> product1 = productRepository.findProductDetailById(product.getId());

        assertNotNull(product1.get());
    }
}
