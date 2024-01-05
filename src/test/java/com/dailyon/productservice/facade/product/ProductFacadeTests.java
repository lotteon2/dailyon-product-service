package com.dailyon.productservice.facade.product;

import com.dailyon.productservice.IntegrationTestSupport;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@AutoConfigureWireMock(port = 0)
public class ProductFacadeTests extends IntegrationTestSupport {

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
