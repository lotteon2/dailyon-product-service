package com.dailyon.productservice.enums;

import com.dailyon.productservice.exception.NotExistsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductType {
    NORMAL("일반"),
    AUCTION("경매"),
    RAFFLE("응모");

    private final String message;

    public static ProductType validate(String name) {
        try {
            return ProductType.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new NotExistsException("존재하지 않는 상품 유형입니다");
        }
    }
}
