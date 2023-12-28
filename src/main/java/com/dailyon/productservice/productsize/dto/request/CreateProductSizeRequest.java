package com.dailyon.productservice.productsize.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductSizeRequest {
    @NotNull(message = "카테고리를 입력해주세요")
    private Long categoryId;

    @NotBlank(message = "치수값을 입력해주세요")
    private String name;
}
