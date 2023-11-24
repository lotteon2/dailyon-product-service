package com.dailyon.productservice.controller.admin;

import com.dailyon.productservice.dto.request.CreateProductRequest;
import com.dailyon.productservice.dto.response.CreateProductResponse;
import com.dailyon.productservice.service.product.ProductService;
import lombok.RequiredArgsConstructor;
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
}
