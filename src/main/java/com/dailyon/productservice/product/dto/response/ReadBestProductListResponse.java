package com.dailyon.productservice.product.dto.response;

import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.vo.BestProductVO;
import dailyon.domain.order.clients.ProductRankResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadBestProductListResponse {
    private List<BestProductVO> responses;

    public static ReadBestProductListResponse create(List<Product> products, List<ProductRankResponse> ranks) {
        List<BestProductVO> responses = new ArrayList<>();
        for(int i=0; i<products.size(); i++) {
            responses.add(BestProductVO.create(products.get(i), ranks.get(i)));
        }
        Collections.sort(responses);

        return ReadBestProductListResponse.builder()
                .responses(responses)
                .build();
    }
}
