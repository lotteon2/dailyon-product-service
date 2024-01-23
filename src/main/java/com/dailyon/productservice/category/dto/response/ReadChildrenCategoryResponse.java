package com.dailyon.productservice.category.dto.response;

import com.dailyon.productservice.category.entity.Category;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadChildrenCategoryResponse {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return '{' + "'id':" + this.id + ", 'name': '" + this.name + "'}";
    }

    public static ReadChildrenCategoryResponse fromEntity(Category category) {
        return ReadChildrenCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
