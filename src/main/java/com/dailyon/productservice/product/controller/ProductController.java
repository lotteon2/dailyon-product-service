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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

    @GetMapping("/search")
    ResponseEntity<ReadProductSearchResponse> searchProducts(@Valid @NotBlank @RequestParam String query) {
        return ResponseEntity.status(HttpStatus.OK).body(productFacade.searchProducts(query));
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
