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
    ResponseEntity<ReadProductSliceResponse> readProductPage(@RequestParam(required = false) Long brandId,
                                                             @RequestParam(required = false) Long categoryId,
                                                             @RequestParam(required = false) Gender gender,
                                                             @RequestParam ProductType type,
                                                             @RequestParam(required = false) String query,
                                                             @PageableDefault(page = 0, size = 8) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.readProductSlice(brandId, categoryId, gender, type, query, pageable));
    }
}
