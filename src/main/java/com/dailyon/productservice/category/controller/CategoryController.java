package com.dailyon.productservice.category.controller;

import com.dailyon.productservice.category.dto.response.ReadBreadCrumbListResponse;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{masterCategoryId}")
    public ResponseEntity<ReadChildrenCategoryListResponse> readChildrenCategories(@PathVariable Long masterCategoryId) {
        return ResponseEntity.ok(categoryService.readChildrenCategories(masterCategoryId));
    }

    @GetMapping("/breadcrumb/{categoryId}")
    public ResponseEntity<ReadBreadCrumbListResponse> readBreadCrumbs(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.readBreadCrumbs(categoryId));
    }
}