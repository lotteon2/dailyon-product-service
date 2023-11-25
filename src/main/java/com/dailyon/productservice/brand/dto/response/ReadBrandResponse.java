package com.dailyon.productservice.brand.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadBrandResponse {
    private Long id;
    private String name;
}
