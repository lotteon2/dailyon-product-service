package com.dailyon.productservice.dto.response;

import com.dailyon.productservice.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadAllCategoryResponse {
    private Long id;
    private String name;
    private Long masterCategoryId;
    private String masterCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReadAllCategoryResponse fromEntity(Category category) {
        return ReadAllCategoryResponse.builder()
                .id(category.getId())
                .masterCategoryId(category.getMasterCategory() == null ? null : category.getMasterCategory().getId())
                .masterCategoryName(category.getMasterCategory() == null ? null : category.getMasterCategory().getName())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
