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
public class ReadBreadCrumbListResponse {
    private List<ReadBreadCrumbResponse> breadCrumbs;

    public static ReadBreadCrumbListResponse fromEntity(List<Category> children) {
        return ReadBreadCrumbListResponse.builder()
                .breadCrumbs(children.stream()
                        .map(ReadBreadCrumbResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
