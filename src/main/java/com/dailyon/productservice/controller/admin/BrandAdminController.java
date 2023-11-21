package com.dailyon.productservice.controller.admin;

import com.dailyon.productservice.dto.request.CreateBrandRequest;
import com.dailyon.productservice.dto.request.UpdateBrandRequest;
import com.dailyon.productservice.dto.response.CreateBrandResponse;
import com.dailyon.productservice.service.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BrandAdminController {
    private final BrandService brandService;

    /*
     TODO : role Authorization 관련 코드 공통화
     */

    @PostMapping("/brands")
    public ResponseEntity<CreateBrandResponse> createBrand(@RequestHeader String role,
                                                           @Valid @RequestBody CreateBrandRequest createBrandRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.createBrand(createBrandRequest));
    }

    @PutMapping("/brands/{brandId}")
    public ResponseEntity<Void> updateBrand(@RequestHeader String role,
                                            @PathVariable Long brandId,
                                            @Valid @RequestBody UpdateBrandRequest updateBrandRequest) {
        brandService.updateBrand(brandId, updateBrandRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
