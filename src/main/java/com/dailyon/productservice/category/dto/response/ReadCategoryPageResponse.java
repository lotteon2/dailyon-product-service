package com.dailyon.productservice.category.dto.response;

import com.dailyon.productservice.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadCategoryPageResponse {
    private Long totalElements;
    private Integer totalPages;

    private List<ReadAllCategoryResponse> responses;

    public static ReadCategoryPageResponse fromEntity(Page<Category> categories) {
        return ReadCategoryPageResponse.builder()
                .totalElements(categories.getTotalElements())
                .totalPages(categories.getTotalPages())
                .responses(categories.stream()
                        .map(ReadAllCategoryResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
