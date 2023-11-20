package com.dailyon.productservice.service.brand;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.dto.request.CreateBrandRequest;
import com.dailyon.productservice.dto.response.CreateBrandResponse;
import com.dailyon.productservice.exception.DuplicatedBrandException;
import com.dailyon.productservice.service.BrandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
public class BrandServiceTests {
    @Autowired
    BrandService brandService;

    @Test
    @DisplayName("브랜드 등록 성공")
    void createBrandServiceSuccess() {
        // given
        String name = "testBrandName";
        CreateBrandRequest createBrandRequest = CreateBrandRequest.builder().name(name).build();

        // when
        CreateBrandResponse createBrandResponse = brandService.createBrand(createBrandRequest);

        // then
        assertNotNull(createBrandResponse);
    }

    @Test
    @DisplayName("브랜드 등록 실패 - 중복 이름")
    void createBrandServiceFail() {
        // given
        String name = "testBrandName";
        CreateBrandRequest createBrandRequest = CreateBrandRequest.builder().name(name).build();
        brandService.createBrand(createBrandRequest);

        // when
        String anotherName = "testBrandName";
        CreateBrandRequest anotherCreateBrandRequest = CreateBrandRequest.builder().name(anotherName).build();

        assertThrows(DuplicatedBrandException.class, () -> brandService.createBrand(anotherCreateBrandRequest));
    }
}
