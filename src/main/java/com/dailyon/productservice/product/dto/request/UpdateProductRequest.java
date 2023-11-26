package com.dailyon.productservice.product.dto.request;

import com.dailyon.productservice.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    @NotNull(message = "브랜드를 입력해주세요")
    private Long brandId;

    @NotNull(message = "카테고리를 입력해주세요")
    private Long categoryId;

    @Min(value = 1, message = "가격은 1원 이상이어야 합니다")
    @Max(value = Integer.MAX_VALUE, message = "가격 범위를 초과했습니다")
    private Integer price;

    @NotBlank(message = "상품 이름을 입력해주세요")
    private String name;

    @NotBlank(message = "성별을 입력해주세요")
    private String gender;

    @NotBlank(message = "상품 코드를 입력해주세요")
    private String code;

    @NotBlank(message = "상품 이미지를 입력해주세요")
    private String image;

    @Valid
    @Size(min = 1, message = "상품 개수를 등록해주세요")
    private List<ProductStockRequest> productStocks;

    @Size(max = 5, message = "상품 설명 이미지는 최대 5개까지 등록 가능합니다")
    private List<String> describeImages;
}
