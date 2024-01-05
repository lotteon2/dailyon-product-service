package com.dailyon.productservice.repository.productstock;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.common.feign.request.OrderProductDto;
import com.dailyon.productservice.common.feign.request.ReadWishCartProductRequest;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.repository.ProductRepository;
import com.dailyon.productservice.productsize.entity.ProductSize;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.dailyon.productservice.productstock.kafka.dto.OrderDto;
import com.dailyon.productservice.productstock.repository.ProductStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductStockRepositoryTests extends IntegrationTestSupport {
    @Autowired
    ProductStockRepository productStockRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductSizeRepository productSizeRepository;

    private Product product;
    private Product product1;
    private List<ProductSize> productSizes = new ArrayList<>();
    private List<ProductStock> productStocks = new ArrayList<>();

    @BeforeEach
    void before() {
        Brand brand = brandRepository.save(Brand.createBrand("brand"));
        Category category = categoryRepository.save(Category.createRootCategory("root"));

        product = productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                "name", "code", "imgUrl", 1000
        ));

        product1 = productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                "name1", "code1", "imgUrl", 2000
        ));

        for(int i=0; i<3; i++) {
            productSizes.add(productSizeRepository.save(ProductSize.create(category, "Product Size_"+i)));
        }

        for(ProductSize productSize: productSizes) {
            productStocks.add(ProductStock.create(product, productSize, 50L));
            productStocks.add(ProductStock.create(product1, productSize, 50L));
        }

        productStocks = productStockRepository.saveAll(productStocks);
    }

    @Test
    @DisplayName("상품 재고 등록 성공")
    void createProductStockSuccess() {
        // given
        List<ProductStock> productStocks = new ArrayList<>();
        for(ProductSize productSize: productSizes) {
            productStocks.add(ProductStock.create(product, productSize, 10L));
        }

        // when
        List<ProductStock> created = productStockRepository.saveAll(productStocks);

        // then
        assertEquals(productStocks.size(), created.size());
    }

    @Test
    @DisplayName("주문 상품 목록 조회 시 상품 정보, 재고 조회")
    void readOrderProductsTest() {
        // given
        List<OrderProductDto> productDtos = new ArrayList<>();
        productDtos.add(OrderProductDto.builder().productId(product.getId()).sizeId(productSizes.get(0).getId()).build());
        productDtos.add(OrderProductDto.builder().productId(product.getId()).sizeId(productSizes.get(1).getId()).build());

        productDtos.add(OrderProductDto.builder().productId(product1.getId()).sizeId(productSizes.get(1).getId()).build());
        productDtos.add(OrderProductDto.builder().productId(product1.getId()).sizeId(productSizes.get(2).getId()).build());
        // when
        List<ProductStock> orderProducts = productStockRepository.findOrderProductsBy(productDtos);

        // then
        assertEquals(productDtos.size(), orderProducts.size());
    }

    @Test
    @DisplayName("찜/장바구니 조회 시 상품 정보, 재고 조회")
    void readWishCartProductsTest() {
        // given
        List<ReadWishCartProductRequest> requests = new ArrayList<>();
        requests.add(ReadWishCartProductRequest.builder().productId(product.getId()).productSizeId(productSizes.get(0).getId()).build());
        requests.add(ReadWishCartProductRequest.builder().productId(product.getId()).productSizeId(productSizes.get(1).getId()).build());
        requests.add(ReadWishCartProductRequest.builder().productId(product1.getId()).productSizeId(productSizes.get(2).getId()).build());

        // when
        List<ProductStock> wishCartProducts = productStockRepository.findWishCartProductsBy(requests);

        // then
        assertEquals(requests.size(), wishCartProducts.size());
    }

    @Test
    @DisplayName("재고 차감 위한 조회 시 select for update")
    void selectForUpdateTest() {
        // given
        List<OrderDto.ProductInfo> productInfos = new ArrayList<>();
        productInfos.add(OrderDto.ProductInfo.builder().productId(product.getId()).sizeId(productSizes.get(0).getId()).build());
        productInfos.add(OrderDto.ProductInfo.builder().productId(product.getId()).sizeId(productSizes.get(1).getId()).build());

        // when
        List<ProductStock> productStocksToUpdate = productStockRepository.selectProductStocksForUpdate(productInfos);

        // then
        assertEquals(productInfos.size(), productStocksToUpdate.size());
    }
}
