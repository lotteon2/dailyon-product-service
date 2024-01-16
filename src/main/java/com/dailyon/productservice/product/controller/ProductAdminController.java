package com.dailyon.productservice.product.controller;

import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.product.dto.request.CreateProductRequest;
import com.dailyon.productservice.product.dto.request.UpdateProductRequest;
import com.dailyon.productservice.product.dto.response.CreateProductResponse;
import com.dailyon.productservice.product.dto.response.ReadProductPageResponse;
import com.dailyon.productservice.product.dto.response.UpdateProductResponse;
import com.dailyon.productservice.product.facade.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ProductAdminController {
    private final ProductFacade productFacade;
    @PostMapping("/products")
    public ResponseEntity<CreateProductResponse> createProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productFacade.createProduct(createProductRequest));
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<UpdateProductResponse> updateProduct(@PathVariable Long productId,
                                                               @RequestBody UpdateProductRequest updateProductRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(productFacade.updateProduct(productId, updateProductRequest));
    }

    @GetMapping("/products")
    ResponseEntity<ReadProductPageResponse> readProductPage(@RequestParam(required = false) Long brandId,
                                                            @RequestParam(required = false) Long categoryId,
                                                            @RequestParam ProductType type,
                                                            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(productFacade.readProductPage(brandId, categoryId, type, pageable));
    }

    @DeleteMapping("/products")
    ResponseEntity<Void> deleteProducts(@RequestParam List<Long> ids) {
        productFacade.deleteProducts(ids);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
