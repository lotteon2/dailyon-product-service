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

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
public class CategoryRepositoryTests {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntityManager entityManager;

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

    @Test
    @DisplayName("master_category_id로 그 하위 카테고리 목록 조회")
    public void findByMasterCategoryId() {
        // given
        Category root1 = categoryRepository.save(Category.createRootCategory("root1"));
        Category root2 = categoryRepository.save(Category.createRootCategory("root2"));

        // when
        List<Category> categories = categoryRepository.findByMasterCategory_Id(null);

        assertEquals(2L, categories.size());
    }

    @Test
    @DisplayName("리프 카테고리 조회")
    public void findLeafCategories() {
        // given
        Category root1 = categoryRepository.save(Category.createRootCategory("root1"));
        Category root2 = categoryRepository.save(Category.createRootCategory("root2"));

        Category mid1 = categoryRepository.save(Category.createCategory(root1, "mid1"));
        Category mid2 = categoryRepository.save(Category.createCategory(root2, "mid2"));

        Category leaf1 = categoryRepository.save(Category.createCategory(mid1, "leaf1"));

        // when
        List<Category> leaves = categoryRepository.findLeafCategories();

        // then
        assertEquals(2, leaves.size()); // mid2와 leaf1
    }

    @Test
    @DisplayName("삭제 이후 리프 카테고리 조회")
    public void findLeafCategoriesAfterDelete() {
        // given
        Category root1 = categoryRepository.save(Category.createRootCategory("root1"));
        Category root2 = categoryRepository.save(Category.createRootCategory("root2"));

        Category mid1 = categoryRepository.save(Category.createCategory(root1, "mid1"));
        Category mid2 = categoryRepository.save(Category.createCategory(root2, "mid2"));

        Category leaf1 = categoryRepository.save(Category.createCategory(mid1, "leaf1"));

        leaf1.softDelete();
        mid2.softDelete();

        entityManager.flush();
        entityManager.clear();

        // when
        List<Category> leaves = categoryRepository.findLeafCategories();

        // then
        assertEquals(2, leaves.size()); // root2와 mid1
    }

    @Test
    @DisplayName("리프 카테고리 조회 - root 카테고리만 있을 때")
    public void findLeafCategories1() {
        // given
        Category root1 = categoryRepository.save(Category.createRootCategory("root1"));
        Category root2 = categoryRepository.save(Category.createRootCategory("root2"));

        // when
        List<Category> leaves = categoryRepository.findLeafCategories();

        // then
        assertEquals(2, leaves.size()); // root1과 root2
    }

    @Test
    @DisplayName("자신을 포함한 모든 자식 카테고리 id 조회")
    public void findAllChildCategoyIds() {
        // given
        Category root1 = categoryRepository.save(Category.createRootCategory("root1"));

        Category mid1 = categoryRepository.save(Category.createCategory(root1, "mid1"));
        Category mid2 = categoryRepository.save(Category.createCategory(root1, "mid2"));

        Category leaf1 = categoryRepository.save(Category.createCategory(mid1, "leaf1"));
        Category leaf2 = categoryRepository.save(Category.createCategory(mid2, "leaf2"));

        // when
        List<Category> allChildCategories = categoryRepository.findAllChildCategories(root1.getId());

        // then
        assertEquals(5, allChildCategories.size());
    }
}
