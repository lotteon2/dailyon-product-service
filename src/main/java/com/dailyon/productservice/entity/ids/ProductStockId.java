package com.dailyon.productservice.entity.ids;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockId implements Serializable {
    private Long product;
    private Long productSize;
}
