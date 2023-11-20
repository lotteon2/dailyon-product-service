package com.dailyon.productservice.service.brand;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.dto.request.CreateBrandRequest;
import com.dailyon.productservice.dto.response.CreateBrandResponse;
import com.dailyon.productservice.dto.response.ReadBrandListResponse;
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

    // TODO : 삭제 api 개발하고 나서 deleted=false만 조회하는지 테스트
    @Test
    @DisplayName("브랜드 전체 조회")
    void readBrandsService() {
        // given
        brandService.createBrand(CreateBrandRequest.builder().name("test1").build());
        brandService.createBrand(CreateBrandRequest.builder().name("test2").build());

        // when
        ReadBrandListResponse brands = brandService.readAllBrands();

        // then
        assertEquals(2, brands.getBrandResponses().size());
    }
}
