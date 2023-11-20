package com.dailyon.productservice.repository.brand;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.entity.Brand;
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
public class BrandRepositoryTests {
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

    // TODO : 삭제 api 개발하고 나서 deleted=false만 조회하는지 테스트
    @Test
    @DisplayName("브랜드 전체 조회")
    void readBrandsRepo() {
        // given
        brandRepository.save(Brand.createBrand("test1"));
        brandRepository.save(Brand.createBrand("test2"));

        // when
        List<Brand> brands = brandRepository.findAllBrands();

        // then
        assertEquals(2, brands.size());
    }
}
