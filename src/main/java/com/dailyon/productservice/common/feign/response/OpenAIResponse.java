package com.dailyon.productservice.common.feign.response;

import com.dailyon.productservice.brand.dto.response.ReadBrandResponse;
import com.dailyon.productservice.category.dto.response.ReadChildrenCategoryResponse;
import com.dailyon.productservice.common.enums.Gender;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OpenAIResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private TokenInfo usage;
    private String system_fingerprint;

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private Long index;
        private Message message;

        @Getter
        @Builder
        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Message {
            private String role;
            private Response content;
            private Object logprobs;
            private String finish_reason;

            @Getter
            @Builder
            @ToString
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Response {
                private CategoryResponse categories;
                private BrandResponse brands;
                private GenderResponse genders;
                private PriceRangeResponse priceRanges;
            }
        }
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryResponse {
        private List<ReadChildrenCategoryResponse> categories;
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrandResponse {
        private List<ReadBrandResponse> brands;
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenderResponse {
        private List<Gender> genders;
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceRangeResponse {
        private List<PriceRange> priceRanges;
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceRange {
        private Long id;
        private String name;
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenInfo {
        private Long prompt_tokens;
        private Long completion_tokens;
        private Long total_tokens;
    }
}
