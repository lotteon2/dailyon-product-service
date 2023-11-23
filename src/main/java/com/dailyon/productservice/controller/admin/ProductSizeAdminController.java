package com.dailyon.productservice.controller.admin;

import com.dailyon.productservice.dto.request.CreateProductSizeRequest;
import com.dailyon.productservice.service.productsize.ProductSizeService;
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
}