package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.vo.NewProductVO;
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
public class ReadNewProductListResponse {
    private List<NewProductVO> responses;

    public static ReadNewProductListResponse create(List<Product> products) {
        return ReadNewProductListResponse.builder()
                .responses(products.stream().map(NewProductVO::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
