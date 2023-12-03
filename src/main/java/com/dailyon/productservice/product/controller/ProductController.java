package com.dailyon.productservice.product.controller;

import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.product.dto.response.ReadProductDetailResponse;
import com.dailyon.productservice.product.dto.response.ReadProductSliceResponse;
import com.dailyon.productservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/id/{productId}")
    ResponseEntity<ReadProductDetailResponse> readProductDetail(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.readProductDetail(productId));
    }

    @GetMapping
    ResponseEntity<ReadProductSliceResponse> readProductSlice(@RequestParam Long lastId,
                                                              @RequestParam(required = false) Long brandId,
                                                              @RequestParam(required = false) Long categoryId,
                                                              @RequestParam(required = false) Gender gender,
                                                              @RequestParam ProductType type) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.readProductSlice(lastId, brandId, categoryId, gender, type));
    }

    @GetMapping("/search")
    ResponseEntity<ReadProductSliceResponse> searchProducts(@RequestParam Long lastId,
                                                            @RequestParam(required = false) String query,
                                                            @RequestParam(required = false) String code) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.searchProductSlice(lastId, query, code));
    }
}
