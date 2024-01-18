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

    /*
     브랜드 이름으로 검색. 관리자 페이지 및 회원 페이지에서 모두 사용할 듯
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ReadBrandListResponse> findBrandsByName(@PathVariable String name) {
        return ResponseEntity.ok(brandService.findBrandsByName(name));
    }
}
