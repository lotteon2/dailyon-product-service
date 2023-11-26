package com.dailyon.productservice.product.controller;

import com.dailyon.productservice.product.dto.response.ReadProductDetailResponse;
import com.dailyon.productservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/id/{productId}")
    ResponseEntity<ReadProductDetailResponse> readProductDetail(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.readProductDetail(productId));
    }
}