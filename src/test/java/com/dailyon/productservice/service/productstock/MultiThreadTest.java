package com.dailyon.productservice.service.productstock;

import com.dailyon.productservice.IntegrationTestSupport;
import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.brand.repository.BrandRepository;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.category.repository.CategoryRepository;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.repository.ProductRepository;
import com.dailyon.productservice.productsize.entity.ProductSize;
import com.dailyon.productservice.productsize.repository.ProductSizeRepository;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.dailyon.productservice.productstock.kafka.dto.OrderDto;
import com.dailyon.productservice.productstock.repository.ProductStockRepository;
import com.dailyon.productservice.productstock.service.ProductStockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

@SpringBootTest
@ActiveProfiles(value = {"test"})
public class MultiThreadTest {
    @Autowired
    ProductStockService productStockService;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductSizeRepository productSizeRepository;

    @Autowired
    ProductStockRepository productStockRepository;

    @Autowired
    ProductRepository productRepository;

    @MockBean
    SimpleMessageListenerContainer simpleMessageListenerContainer;

    @MockBean
    QueueMessagingTemplate queueMessagingTemplate;

    @AfterEach
    void afterEach() {
        productStockRepository.deleteAllInBatch();
        productSizeRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        brandRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("멀티스레드 환경에서도 재고만큼만 차감된다")
    void multiThreadTests() throws Exception {
        int applicantsCount = 1000;

        Brand brand = brandRepository.save(Brand.createBrand("brand"));
        Category category = categoryRepository.save(Category.createRootCategory("category"));

        ProductSize productSize = productSizeRepository.save(ProductSize.create(category, "productSize1"));
        Product product = productRepository.save(Product.create(
                brand, category, ProductType.NORMAL, Gender.COMMON,
                "TEST_NAME", "TEST_CODE", "TEST_IMG_URL", 1000)
        );

        List<ProductStock> createProductStocks = new ArrayList<>();
        createProductStocks.add(ProductStock.create(product, productSize, (long) applicantsCount));
        productStockRepository.saveAll(createProductStocks);

        List<OrderDto.ProductInfo> list = new ArrayList<>();
        list.add(OrderDto.ProductInfo.builder().productId(product.getId()).sizeId(productSize.getId()).quantity(1L).build());

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(applicantsCount);

        LongStream.rangeClosed(0, applicantsCount).forEach(i -> executorService.execute(() -> {
            try {
                productStockService.deductProductStocks(list);
            } catch(Exception e) {
                // do nothing
            } finally {
                latch.countDown();
            }
        }));
        latch.await();

        List<ProductStock> productStocks = productStockRepository.findProductStocksByProductOrderByProductSize(product);
        Assertions.assertEquals(0, productStocks.get(0).getQuantity());
    }
}
