package com.dailyon.productservice.category.dto.response;

import com.dailyon.productservice.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCategoryResponse {
    private Long categoryId;

    public static CreateCategoryResponse fromEntity(Category category) {
        return CreateCategoryResponse.builder()
                .categoryId(category.getId())
                .build();
    }
}
