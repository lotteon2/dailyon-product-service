package com.dailyon.productservice.controller;

import com.dailyon.productservice.dto.response.ReadBrandListResponse;
import com.dailyon.productservice.service.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping("")
    public ResponseEntity<ReadBrandListResponse> readBrands() {
        return ResponseEntity.ok(brandService.readAllBrands());
    }
}
