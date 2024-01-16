package com.dailyon.productservice.brand.controller;

import com.dailyon.productservice.brand.dto.request.CreateBrandRequest;
import com.dailyon.productservice.brand.dto.request.UpdateBrandRequest;
import com.dailyon.productservice.brand.dto.response.CreateBrandResponse;
import com.dailyon.productservice.brand.dto.response.ReadBrandPageResponse;
import com.dailyon.productservice.brand.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BrandAdminController {
    private final BrandService brandService;

    @PostMapping("/brands")
    public ResponseEntity<CreateBrandResponse> createBrand(@Valid @RequestBody CreateBrandRequest createBrandRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.createBrand(createBrandRequest));
    }

    @GetMapping("/page/brands")
    public ResponseEntity<ReadBrandPageResponse> readBrandPages(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(brandService.readBrandPage(pageable));
    }

    @PutMapping("/brands/{brandId}")
    public ResponseEntity<Void> updateBrand(@PathVariable Long brandId,
                                            @Valid @RequestBody UpdateBrandRequest updateBrandRequest) {
        brandService.updateBrand(brandId, updateBrandRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/brands/{brandId}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
