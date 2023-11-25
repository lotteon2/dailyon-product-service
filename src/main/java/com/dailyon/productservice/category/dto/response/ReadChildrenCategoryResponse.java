package com.dailyon.productservice.category.dto.response;

import com.dailyon.productservice.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadChildrenCategoryResponse {
    private Long id;
    private String name;

    public static ReadChildrenCategoryResponse fromEntity(Category category) {
        return ReadChildrenCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
