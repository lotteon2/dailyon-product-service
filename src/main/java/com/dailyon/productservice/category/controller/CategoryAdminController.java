package com.dailyon.productservice.category.controller;

import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.dto.request.UpdateCategoryRequest;
import com.dailyon.productservice.category.dto.response.ReadAllCategoryListResponse;
import com.dailyon.productservice.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<Void> createCategory(@RequestHeader String role,
                                               @Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/categories")
    public ResponseEntity<ReadAllCategoryListResponse> readAllCategories(@RequestHeader String role) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.readAllCategories());
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Void> updateCategory(@RequestHeader String role,
                                               @PathVariable Long categoryId,
                                               @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        categoryService.updateCategoryName(categoryId, updateCategoryRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
