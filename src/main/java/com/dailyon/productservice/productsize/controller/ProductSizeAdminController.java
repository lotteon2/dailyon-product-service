package com.dailyon.productservice.productsize.controller;

import com.dailyon.productservice.productsize.dto.request.CreateProductSizeRequest;
import com.dailyon.productservice.productsize.dto.request.UpdateProductSizeRequest;
import com.dailyon.productservice.productsize.dto.response.ReadProductSizeListResponse;
import com.dailyon.productservice.productsize.service.ProductSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ProductSizeAdminController {
    private final ProductSizeService productSizeService;

    @PostMapping("/product-size")
    public ResponseEntity<Void> createProductSize(@RequestHeader String role,
                                                  @Valid @RequestBody CreateProductSizeRequest createProductSizeRequest) {
        productSizeService.createProductSize(createProductSizeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/product-size/{categoryId}")
    public ResponseEntity<ReadProductSizeListResponse> readProductSizeList(@RequestHeader String role,
                                                                           @PathVariable Long categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(productSizeService.readProductSizeListByCategory(categoryId));
    }

    @PutMapping("/product-size/{productSizeId}")
    public ResponseEntity<Void> updateProductSizeName(@RequestHeader String role,
                                                      @PathVariable Long productSizeId,
                                                      @Valid @RequestBody UpdateProductSizeRequest updateProductSizeRequest) {
        productSizeService.updateProductSizeName(productSizeId, updateProductSizeRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
