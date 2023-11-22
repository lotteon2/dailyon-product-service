package com.dailyon.productservice.repository.productsize;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.entity.Category;
import com.dailyon.productservice.entity.ProductSize;
import com.dailyon.productservice.repository.category.CategoryRepository;
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
public class ProductSizeRepositoryTests {
    @Autowired
    ProductSizeRepository productSizeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void before() {
        categoryRepository.save(Category.createRootCategory("TEST"));
    }

    @Test
    @DisplayName("치수 생성 성공")
    public void createProductSizeSuccess() {
        ProductSize productSize =
                productSizeRepository.save(ProductSize.create(categoryRepository.findById(1L).get(), "TEST"));

        assertEquals("TEST", productSize.getName());
        assertEquals(1L, productSize.getCategory().getId());
    }
}
