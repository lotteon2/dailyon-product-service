package com.dailyon.productservice.service.brand;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.productservice.dto.request.CreateBrandRequest;
import com.dailyon.productservice.dto.request.UpdateBrandRequest;
import com.dailyon.productservice.dto.response.CreateBrandResponse;
import com.dailyon.productservice.dto.response.ReadBrandListResponse;
import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.exception.UniqueException;
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

        assertThrows(UniqueException.class, () -> brandService.createBrand(anotherCreateBrandRequest));
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

    @Test
    @DisplayName("브랜드 이름 수정 성공")
    void updateBrandServiceSuccess() {
        // given
        String name = "testBrandName";
        CreateBrandRequest createBrandRequest = CreateBrandRequest.builder().name(name).build();
        CreateBrandResponse createBrandResponse = brandService.createBrand(createBrandRequest);

        UpdateBrandRequest updateBrandRequest = UpdateBrandRequest.builder().name("test1").build();

        // when
        brandService.updateBrand(createBrandResponse.getBrandId(), updateBrandRequest);

        // then
        assertEquals(brandService.readAllBrands().getBrandResponses().get(0).getName(), "test1");
    }

    @Test
    @DisplayName("브랜드 이름 수정 실패 - 존재하지 않는 id")
    void updateBrandServiceFail1() {
        // given
        UpdateBrandRequest updateBrandRequest = UpdateBrandRequest.builder().name("test").build();

        // when, then
        assertThrows(NotExistsException.class, () -> brandService.updateBrand(1L, updateBrandRequest));
    }

    @Test
    @DisplayName("브랜드 이름 수정 실패 - 중복 이름")
    void updateBrandServiceFail2() {
        // given
        String name = "test";
        CreateBrandRequest createBrandRequest = CreateBrandRequest.builder().name(name).build();
        brandService.createBrand(createBrandRequest);

        String name1 = "test1";
        CreateBrandRequest createBrandRequest1 = CreateBrandRequest.builder().name(name1).build();
        CreateBrandResponse createBrandResponse = brandService.createBrand(createBrandRequest1);

        // when
        UpdateBrandRequest updateBrandRequest = UpdateBrandRequest.builder().name(name).build();

        // then
        assertThrows(UniqueException.class, () ->
                brandService.updateBrand(createBrandResponse.getBrandId(), updateBrandRequest)
        );
    }
}
