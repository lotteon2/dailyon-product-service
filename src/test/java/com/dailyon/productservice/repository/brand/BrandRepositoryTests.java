package com.dailyon.productservice.repository.brand;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.brand.entity.Brand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class BrandRepositoryTests extends IntegrationTestSupport {
    @Autowired
    BrandRepository brandRepository;

    @Test
    @DisplayName("브랜드 등록 성공")
    void createBrandRepoSuccess() {
        // given
        String name = "testBrandName";

        // when
        Brand createdBrand = brandRepository.save(Brand.createBrand(name));

        // then
        assertEquals(name, createdBrand.getName());
        assertFalse(createdBrand.isDeleted());
        assertNotNull(createdBrand.getCreatedAt());
        assertNotNull(createdBrand.getUpdatedAt());
    }

    @Test
    @DisplayName("브랜드 이름 검색")
    void findsByNameTest() {
        // given
        String name = "testBrandName";

        // when
        Brand createdBrand = brandRepository.save(Brand.createBrand(name));

        List<Brand> test = brandRepository.findBrandsByNameContainsOrderByName("test");

        assertEquals("testBrandName",test.get(0).getName());
    }


    @Test
    @DisplayName("브랜드 전체 조회")
    void readBrandsRepo() {
        // given
        brandRepository.save(Brand.createBrand("test1"));
        brandRepository.save(Brand.createBrand("test2"));

        // when
        List<Brand> brands = brandRepository.findAll();

        // then
        assertEquals(2, brands.size());
    }
}
