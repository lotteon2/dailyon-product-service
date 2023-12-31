package com.dailyon.productservice.repository.productsize;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.productsize.entity.ProductSize;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductSizeRepositoryTests extends IntegrationTestSupport {
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
        List<ProductSize> result = productSizeRepository.findProductSizesByCategoryId(category.getId());

        // then
        assertEquals(result.size(), productSizes.size());
    }

    @Test
    @DisplayName("카테고리에 해당하는 치수 페이징 조회")
    public void readProductSizePage() {
        // given
        Long categoryId = category.getId();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "updatedAt"));

        // when
        Page<ProductSize> result = productSizeRepository.findProductSizePagesByCategoryId(categoryId, pageable);

        // then
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
    }

    @Test
    @DisplayName("카테고리에 해당하는 치수 삭제")
    public void deleteProductSizesByCategory() {
        // given, when
        List<Category> toDelete = new ArrayList<>();
        toDelete.add(category);
        productSizeRepository.deleteProductSizesByCategory(toDelete);

        // then
        assertEquals(0, productSizeRepository.findProductSizesByCategoryId(category.getId()).size());
    }
}
