package com.dailyon.productservice.category.controller;

import com.dailyon.productservice.category.dto.response.ReadBreadCrumbListResponse;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryListResponse;
import com.dailyon.productservice.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * master_category_id가 {masterCategoryId}인 카테고리의 목록을 반환
     */
    @GetMapping("/master")
    public ResponseEntity<ReadChildrenCategoryListResponse> readCategoriesByMaster(@RequestParam(required = false) Long masterCategoryId) {
        return ResponseEntity.ok(categoryService.readChildrenCategoriesByMaster(masterCategoryId));
    }

    /**
     * {id}라는 id를 가지는 카테고리의 자식 카테고리의 목록을 반환
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<ReadChildrenCategoryListResponse> readChildren(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.readChildrenCategoriesOf(id));
    }

    @GetMapping("/breadcrumb/{categoryId}")
    public ResponseEntity<ReadBreadCrumbListResponse> readBreadCrumbs(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.readBreadCrumbs(categoryId));
    }
}
