package com.dailyon.productservice.product.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Builder
public class ReadOrderProductRequest {
    @Valid
    @Size(min = 1)
    private List<ReadOrderProductRequest.ProductDto> productRequest;

    @Builder
    @Getter
    public static class ProductDto {
        @NotNull
        private Long productId;
        @NotNull
        private Long sizeId;
    }
}
