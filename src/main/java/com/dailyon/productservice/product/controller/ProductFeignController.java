package com.dailyon.productservice.product.controller;

import com.dailyon.productservice.product.dto.response.ReadOOTDProductListResponse;
import com.dailyon.productservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ProductFeignController {
    private final ProductService productService;

    @GetMapping("/post-image/products")
    ResponseEntity<ReadOOTDProductListResponse> readOOTDProductDetail(@RequestParam List<Long> id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.readOOTDProductDetails(id));
    }
}
