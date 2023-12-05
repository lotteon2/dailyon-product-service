package com.dailyon.productservice.brand.controller;

import com.dailyon.productservice.brand.dto.response.ReadBrandListResponse;
import com.dailyon.productservice.brand.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    // TODO : Pagination
    @GetMapping("")
    public ResponseEntity<ReadBrandListResponse> readBrands() {
        return ResponseEntity.ok(brandService.readAllBrands());
    }
}
