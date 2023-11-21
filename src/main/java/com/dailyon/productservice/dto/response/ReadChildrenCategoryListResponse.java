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
public class ReadChildrenCategoryListResponse {
    private List<ReadChildrenCategoryResponse> categoryResponses;

    public static ReadChildrenCategoryListResponse fromEntity(List<Category> childrenCategories) {
        return ReadChildrenCategoryListResponse.builder()
                .categoryResponses(childrenCategories.stream()
                        .map(children -> new ReadChildrenCategoryResponse(children.getId(), children.getName()))
                        .collect(Collectors.toList()))
                .build();
    }
}
