package com.dailyon.productservice.category.controller;

import com.dailyon.productservice.category.dto.request.CreateCategoryRequest;
import com.dailyon.productservice.category.dto.request.UpdateCategoryRequest;
import com.dailyon.productservice.category.dto.response.CreateCategoryResponse;
import com.dailyon.productservice.category.dto.response.ReadAllCategoryListResponse;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*") // TODO : gateway 이후 삭제
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<CreateCategoryResponse> createCategory(@RequestHeader String role,
                                                                 @Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(createCategoryRequest));
    }

    @GetMapping("/categories")
    public ResponseEntity<ReadAllCategoryListResponse> readAllCategories(@RequestHeader String role) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.readAllCategories());
    }

    /*
     TODO : 리프 카테고리 조회 -> 관리자 사용
     */
    @GetMapping("/categories/leaf")
    public ResponseEntity<ReadChildrenCategoryListResponse> readLeafCategories(@RequestHeader String role) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.readLeafCategories());
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Void> updateCategory(@RequestHeader String role,
                                               @PathVariable Long categoryId,
                                               @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        categoryService.updateCategoryName(categoryId, updateCategoryRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
