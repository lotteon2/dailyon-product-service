package com.dailyon.productservice.common.feign.response;

import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private Message message;
        private Object logprobs;
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private List<ReadChildrenCategoryResponse> categories;
        private List<ReadBrandResponse> brands;
        private List<Gender> genders;
        private List<PriceRange> priceRanges;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadChildrenCategoryResponse {
        private Long id;
        private String name;

        @Override
        public String toString() {
            return "{'id':" + this.id + ", 'name':" + "'" + this.name + "'" +"}";
        }

        public static ReadChildrenCategoryResponse fromEntity(Category category) {
            return ReadChildrenCategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadBrandResponse {
        private Long id;
        private String name;

        @Override
        public String toString() {
            return "{'id':" + this.id + ", 'name':" + "'" + this.name + "'" +"}";
        }

        public static ReadBrandResponse fromEntity(Brand brand) {
            return ReadBrandResponse.builder()
                    .id(brand.getId())
                    .name(brand.getName())
                    .build();
        }
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceRange {
        private Long id;
        private String name;

        public Integer[] parseHighAndLow() {
            String[] split = this.name.replace("$", "").split("-");
            return new Integer[] { Integer.parseInt(split[0]) * 1000, Integer.parseInt(split[1]) * 1000 };
        }
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private Long promptTokens;

        @JsonProperty("completion_tokens")
        private Long completionTokens;

        @JsonProperty("total_tokens")
        private Long totalTokens;
    }
}
