package com.dailyon.productservice.common.feign.request;

import com.dailyon.productservice.product.entity.Product;
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
public class MultipleProductCouponsRequest {
    private List<ProductCategoryPair> products;

    public static MultipleProductCouponsRequest fromEntity(List<Product> products) {
        return MultipleProductCouponsRequest.builder()
                .products(products.stream()
                        .map(ProductCategoryPair::fromEntity)
                        .collect(Collectors.toList())
                ).build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCategoryPair {
        private Long productId;
        private Long categoryId;

        public static ProductCategoryPair fromEntity(Product product) {
            return ProductCategoryPair.builder()
                    .productId(product.getId())
                    .categoryId(product.getCategory().getId())
                    .build();
        }
    }
}
