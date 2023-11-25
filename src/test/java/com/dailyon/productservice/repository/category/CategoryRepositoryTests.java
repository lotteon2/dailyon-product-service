package com.dailyon.productservice.repository.category;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
public class CategoryRepositoryTests {
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("최상위 카테고리 등록")
    public void createRootCategorySuccess() {
        // given
        String name = "test";

        // when
        Category category = categoryRepository.save(Category.createRootCategory(name));

        // then
        assertEquals(category.getName(), name);
        assertNull(category.getMasterCategory());
    }

    @Test
    @DisplayName("하위 카테고리 등록")
    public void createCategorySuccess() {
        // given
        String master = "master";
        Category masterCategory = categoryRepository.save(Category.createRootCategory(master));

        // when
        String name = "name";
        Category category = categoryRepository.save(Category.createCategory(masterCategory, name));

        // then
        assertEquals(category.getMasterCategory(), masterCategory);
        assertEquals(category.getName(), name);
    }

    @Test
    @DisplayName("전체 카테고리 조회")
    public void findAllCategories() {
        // given
        Category root = categoryRepository.save(Category.createRootCategory("root"));
        Category mid = categoryRepository.save(Category.createCategory(root, "mid"));
        Category leaf = categoryRepository.save(Category.createCategory(mid, "leaf"));

        // when
        List<Category> categories = categoryRepository.findAll();

        // then
        assertEquals(3, categories.size());
    }
}
