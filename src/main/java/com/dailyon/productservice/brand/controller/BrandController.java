package com.dailyon.productservice.brand.controller;

import com.dailyon.productservice.brand.dto.response.ReadBrandListResponse;
import com.dailyon.productservice.brand.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping("")
    public ResponseEntity<ReadBrandListResponse> readBrands() {
        return ResponseEntity.ok(brandService.readAllBrands());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ReadBrandListResponse> findBrandsByName(@PathVariable String name) {
        return ResponseEntity.ok(brandService.findBrandsByName(name));
    }
}
