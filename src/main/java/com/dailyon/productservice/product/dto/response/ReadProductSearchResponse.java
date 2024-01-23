package com.dailyon.productservice.product.dto.response;

import static com.dailyon.productservice.common.feign.response.MultipleProductCouponsResponse.CouponInfoItemResponse;
import com.dailyon.productservice.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadProductSearchResponse {
    private List<ReadProductResponse> productResponses;

    public static ReadProductSearchResponse create(List<Product> products, Map<Long, List<CouponInfoItemResponse>> coupons) {
        return ReadProductSearchResponse.builder()
                .productResponses(products.stream()
                        .map(product -> ReadProductResponse.create(product, coupons.get(product.getId())))
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadProductResponse {
        private Long id;
        private String brandName;
        private String categoryName;
        private Integer price;
        private String name;
        private String code;
        private String imgUrl;
        private Double avgRating;
        private Long reviewCount;
        private LocalDateTime createdAt;
        private List<CouponInfoItemResponse> coupons;

        public static ReadProductResponse create(Product product, List<CouponInfoItemResponse> coupons) {
            return ReadProductResponse.builder()
                    .id(product.getId())
                    .brandName(product.getBrand().getName())
                    .categoryName(product.getCategory().getName())
                    .name(product.getName())
                    .price(product.getPrice())
                    .code(product.getCode())
                    .imgUrl(product.getImgUrl())
                    .avgRating(product.getReviewAggregate().getAvgRating())
                    .reviewCount(product.getReviewAggregate().getReviewCount())
                    .createdAt(product.getCreatedAt())
                    .coupons(coupons)
                    .build();
        }
    }
}
