package com.dailyon.productservice.service.brand;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.brand.dto.request.CreateBrandRequest;
import com.dailyon.productservice.brand.dto.request.UpdateBrandRequest;
import com.dailyon.productservice.brand.dto.response.CreateBrandResponse;
import com.dailyon.productservice.brand.dto.response.ReadBrandListResponse;
import com.dailyon.productservice.brand.dto.response.ReadBrandPageResponse;
import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.brand.service.BrandService;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.common.exception.DeleteException;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.exception.UniqueException;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

public class BrandServiceTests extends IntegrationTestSupport {
    @Autowired
    BrandService brandService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    EntityManager entityManager;

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

    @Test
    @DisplayName("페이지네이션 브랜드 조회")
    void readBrandPageTest() {
        // given
        for(int i=0; i<5; i++) {
            brandService.createBrand(CreateBrandRequest.builder().name("test_"+i).build());
        }
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "updatedAt"));

        // when
        ReadBrandPageResponse response = brandService.readBrandPage(pageRequest);

        // then
        assertEquals(1, response.getTotalPages());
        assertEquals(5, response.getTotalElements());
        assertEquals("test_4", response.getBrandResponses().get(0).getName());
    }

    @Test
    @DisplayName("브랜드 삭제 성공")
    void deleteBrandSuccess() {
        // given
        Brand brand = brandRepository.save(Brand.createBrand("test"));
        Category category = categoryRepository.save(Category.createRootCategory("root"));
        Product product = productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                "name", "code", "imgUrl", 1000
        ));
        product.softDelete();

        entityManager.flush();
        entityManager.clear();

        // when, then
        assertDoesNotThrow(() -> brandService.deleteBrand(brand.getId()));
    }

    @Test
    @DisplayName("브랜드 삭제 실패 - 존재하지 않는 브랜드")
    void deleteBrandFail1() {
        // given
        Brand brand = brandRepository.save(Brand.createBrand("test"));
        Category category = categoryRepository.save(Category.createRootCategory("root"));
        Product product = productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                "name", "code", "imgUrl", 1000
        ));
        entityManager.flush();
        entityManager.clear();

        // when, then
        assertThrows(NotExistsException.class, () -> brandService.deleteBrand(0L));
    }

    @Test
    @DisplayName("브랜드 삭제 실패 - 상품 존재")
    void deleteBrandFail2() {
        // given
        Brand brand = brandRepository.save(Brand.createBrand("test"));
        Category category = categoryRepository.save(Category.createRootCategory("root"));
        Product product = productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                "name", "code", "imgUrl", 1000
        ));
        entityManager.flush();
        entityManager.clear();

        // when, then
        assertThrows(DeleteException.class, () -> brandService.deleteBrand(brand.getId()));
    }
}
