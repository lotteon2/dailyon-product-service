package com.dailyon.productservice.facade.product;

import com.dailyon.productservice.common.exception.DeleteException;
import com.dailyon.productservice.product.facade.ProductFacade;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@ActiveProfiles(value = {"test"})
public class ProductFacadeTests {

    /*@Autowired
    ProductFacade productFacade;

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProductSuccess() throws Exception {
        // given
        Path file = ResourceUtils.getFile("classpath:payload/coupon-existence-success.json").toPath();

        stubFor(get(urlEqualTo("/clients/coupons/coupon-existence?productIds=1%2C2"))
                .willReturn(aResponse().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(Files.readAllBytes(file))));

        // when
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        // then
        productFacade.deleteProducts(ids);
    }

    @Test
    @DisplayName("상품 삭제 실패")
    void deleteProductFail() throws Exception {
        // given
        Path file = ResourceUtils.getFile("classpath:payload/coupon-existence-fail.json").toPath();

        stubFor(get(urlEqualTo("/clients/coupons/coupon-existence?productIds=1%2C2"))
                .willReturn(aResponse().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(Files.readAllBytes(file))));

        // when
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        // then
        assertThrows(DeleteException.class, () -> productFacade.deleteProducts(ids));
    }*/
}
