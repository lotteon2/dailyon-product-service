package com.dailyon.productservice.product.controller;

import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.product.dto.request.CreateProductRequest;
import com.dailyon.productservice.product.dto.request.UpdateProductRequest;
import com.dailyon.productservice.product.dto.response.CreateProductResponse;
import com.dailyon.productservice.product.dto.response.ReadProductPageResponse;
import com.dailyon.productservice.product.dto.response.UpdateProductResponse;
import com.dailyon.productservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ProductAdminController {
    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<CreateProductResponse> createProduct(@RequestHeader String role,
                                                               @Valid @RequestBody CreateProductRequest createProductRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(createProductRequest));
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<UpdateProductResponse> updateProduct(@RequestHeader String role,
                                                               @PathVariable Long productId,
                                                               @RequestBody UpdateProductRequest updateProductRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productId, updateProductRequest));
    }

    @GetMapping("/products")
    ResponseEntity<ReadProductPageResponse> readProductPage(@RequestHeader String role,
                                                            @RequestParam(required = false) Long brandId,
                                                            @RequestParam(required = false) Long categoryId,
                                                            @RequestParam ProductType type,
                                                            @PageableDefault Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.readProductPage(brandId, categoryId, type, pageable));
    }
}
