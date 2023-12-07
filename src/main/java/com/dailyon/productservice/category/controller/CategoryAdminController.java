package com.dailyon.productservice.category.controller;

import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.dto.request.UpdateCategoryRequest;
import com.dailyon.productservice.category.dto.response.CreateCategoryResponse;
import com.dailyon.productservice.category.dto.response.ReadAllCategoryListResponse;
import com.dailyon.productservice.category.dto.response.ReadCategoryPageResponse;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.category.service.CategoryService;
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
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<CreateCategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(createCategoryRequest));
    }

    @GetMapping("/categories")
    public ResponseEntity<ReadAllCategoryListResponse> readAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.readAllCategories());
    }

    @GetMapping("/page/categories")
    public ResponseEntity<ReadCategoryPageResponse> readCategoryPages(@PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.readCategoryPages(pageable));
    }

    @GetMapping("/categories/leaf")
    public ResponseEntity<ReadChildrenCategoryListResponse> readLeafCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.readLeafCategories());
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId,
                                               @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        categoryService.updateCategoryName(categoryId, updateCategoryRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
