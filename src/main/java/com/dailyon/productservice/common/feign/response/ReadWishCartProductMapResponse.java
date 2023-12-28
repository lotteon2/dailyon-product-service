package com.dailyon.productservice.common.feign.response;

import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.productstock.entity.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadWishCartProductMapResponse {
    private Map<String, ReadWishCartProductResponse> responses;

    public static ReadWishCartProductMapResponse fromEntity(List<ProductStock> productStocks) {
        return ReadWishCartProductMapResponse.builder()
                .responses(productStocks.stream().collect(
                        Collectors.toMap(ReadWishCartProductResponse::toKey, ReadWishCartProductResponse::fromEntity))
                )
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadWishCartProductResponse {
        private Long productId;
        private Long productSizeId;
        private String productSizeName;
        private String brandName;
        private String productName;
        private String gender;
        private Long productQuantity;
        private Integer productPrice;
        private String imgUrl;

        public static String toKey(ProductStock productStock) {
            return "pid=" + productStock.getProduct().getId() + "&sid=" + productStock.getProductSize().getId();
        }

        public static ReadWishCartProductResponse fromEntity(ProductStock productStock) {
            return ReadWishCartProductResponse.builder()
                    .productId(productStock.getProduct().getId())
                    .productSizeId(productStock.getProductSize().getId())
                    .productSizeName(productStock.getProductSize().getName())
                    .brandName(productStock.getProduct().getBrand().getName())
                    .productName(productStock.getProduct().getName())
                    .gender(productStock.getProduct().getGender().getMessage())
                    .productQuantity(productStock.getQuantity())
                    .productPrice(productStock.getProduct().getPrice())
                    .imgUrl(productStock.getProduct().getImgUrl())
                    .build();
        }
    }
}
