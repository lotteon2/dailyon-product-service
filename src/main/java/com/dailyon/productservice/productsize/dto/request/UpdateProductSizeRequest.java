package com.dailyon.productservice.productsize.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductSizeRequest {
    @NotBlank(message = "치수값을 입력해주세요")
    private String name;
}
