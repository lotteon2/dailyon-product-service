package com.dailyon.productservice.repository.brand;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.entity.Brand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

}
