package com.dailyon.productservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    private Long masterCategoryId;

    @NotBlank(message = "카테고리명을 입력해주세요")
    private String categoryName;
}
