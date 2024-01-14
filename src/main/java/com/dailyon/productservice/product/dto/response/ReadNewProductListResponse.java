package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.product.vo.NewProductVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadNewProductListResponse {
    private List<NewProductVO> responses;
}
