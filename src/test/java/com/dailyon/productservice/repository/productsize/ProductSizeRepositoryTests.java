package com.dailyon.productservice.repository.productsize;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.productsize.entity.ProductSize;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
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
public class ProductSizeRepositoryTests {
    @Autowired
    ProductSizeRepository productSizeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntityManager entityManager;

    Category category;
    List<ProductSize> productSizes = new ArrayList<>();

    @BeforeEach
    void before() {
        category = categoryRepository.save(Category.createRootCategory("TEST"));

        for(int i=0; i<3; i++) {
            productSizes.add(productSizeRepository.save(ProductSize.create(category, "Product Size_"+i)));
        }

        entityManager.clear();
    }

    @Test
    @DisplayName("치수 등록 성공")
    public void createProductSizeSuccess() {
        ProductSize productSize =
                productSizeRepository.save(ProductSize.create(categoryRepository.findById(category.getId()).get(), "TEST"));

        assertEquals("TEST", productSize.getName());
        assertEquals(category.getId(), productSize.getCategory().getId());
    }

    @Test
    @DisplayName("카테고리에 해당하는 치수 목록 조회")
    public void readProductSizeList() {
        // given, when
        List<ProductSize> result = productSizeRepository.readProductSizesByCategoryId(category.getId());

        // then
        assertEquals(result.size(), productSizes.size());
    }
}
