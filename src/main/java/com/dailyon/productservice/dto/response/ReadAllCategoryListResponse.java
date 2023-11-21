package com.dailyon.productservice.dto.response;

import com.dailyon.productservice.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadAllCategoryListResponse {
    private List<ReadAllCategoryResponse> allCategories;

    public static ReadAllCategoryListResponse fromEntity(List<Category> categories) {
        return ReadAllCategoryListResponse.builder()
                .allCategories(categories.stream()
                        .map(ReadAllCategoryResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
