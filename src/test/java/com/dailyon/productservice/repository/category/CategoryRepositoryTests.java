package com.dailyon.productservice.repository.category;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.entity.Category;
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
    @DisplayName("최상위 카테고리의 하위 카테고리 목록 조회")
    public void findChildrenCategoriesFromRoot() {
        // given
        Category masterCategory = categoryRepository.save(Category.createRootCategory("master"));
        for(int i=0; i<3; i++) {
            categoryRepository.save(Category.createCategory(masterCategory, "children_"+i));
        }

        // when
        List<Category> children = categoryRepository.findChildrenCategoriesById(masterCategory.getId());

        // then
        assertEquals(3, children.size());
    }

    @Test
    @DisplayName("최하위 카테고리의 하위 카테고리 목록 조회")
    public void findChildrenCategoriesFromLeaf() {
        // given
        Category masterCategory = categoryRepository.save(Category.createRootCategory("master"));

        // when
        List<Category> children = categoryRepository.findChildrenCategoriesById(masterCategory.getId());

        // then
        assertEquals(0, children.size());
    }

    @Test
    @DisplayName("전체 카테고리 조회")
    public void findAllCategories() {
        // given
        categoryRepository.save(Category.createRootCategory("name"));

        // when
        List<Category> categories = categoryRepository.findAll();

        // then
        assertEquals(1, categories.size());
    }
}
