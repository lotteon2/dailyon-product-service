package com.dailyon.productservice.product.controller;

import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.product.dto.response.*;
import com.dailyon.productservice.product.facade.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductFacade productFacade;

    @GetMapping("/id/{productId}")
    ResponseEntity<ReadProductDetailResponse> readProductDetail(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productFacade.readProductDetail(productId));
    }

    @GetMapping("/auctions/id/{productId}")
    ResponseEntity<ReadProductDetailResponse> readAuctionProductDetail(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productFacade.readAuctionProductDetail(productId));
    }
    /**
     * 쇼핑몰 화면에서 무한 스크롤 위한 조회 api
     * @param lastVal 최초 호출 시 direction이 asc면 0, desc면 큰 값
     * @param type 상품 타입(NORMAL, AUCTION)
     * @param brandId 브랜드 id
     * @param categoryId 카테고리 id(카테고리 지정하면 하위 카테고리들 포함)
     * @param gender 성별(MALE, FEMALE, COMMON)
     * @param lowPrice 가격 필터 하한
     * @param highPrice 가격 필터 상한
     * @param sort 정렬 기준(price, review, rating)
     * @param direction 오름/내림차순(asc, desc)
     * @param query 상품명 또는 코드
     * @return hasNext, List -> List의 마지막값을 lastVal에 저장하고 다음 요청에 포함시켜야 함.
     */
    @GetMapping
    ResponseEntity<ReadProductSliceResponse> readProductSlice(
            @RequestParam(required = false, defaultValue = "NORMAL") ProductType type,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Integer lowPrice,
            @RequestParam(required = false) Integer highPrice,
            @RequestParam(required = false) String query,
            @RequestParam int page,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                productFacade.readProductSlice(
                        brandId, categoryId, gender, type,
                        lowPrice, highPrice, query, page, sort, direction
                )
        );
    }

    @GetMapping("/search/ootd")
    ResponseEntity<ReadOOTDSearchSliceResponse> searchProductsFromOOTD(@RequestParam Long lastId,
                                                                       @RequestParam String query) {
        return ResponseEntity.status(HttpStatus.OK).body(productFacade.searchFromOOTD(lastId, query));
    }

    @GetMapping("/new")
    ResponseEntity<ReadNewProductListResponse> readNewProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productFacade.readNewProducts());
    }

    @GetMapping("/best")
    ResponseEntity<ReadBestProductListResponse> readBestProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productFacade.readBestProducts());
    }
}
