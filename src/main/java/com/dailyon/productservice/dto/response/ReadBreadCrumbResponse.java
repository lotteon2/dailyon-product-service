package com.dailyon.productservice.dto.response;

import com.dailyon.productservice.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadBreadCrumbResponse {
    private Long id;
    private String name;

    public static ReadBreadCrumbResponse fromEntity(Category category) {
        return ReadBreadCrumbResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
