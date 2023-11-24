package com.dailyon.productservice.enums;

import com.dailyon.productservice.exception.NotExistsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    COMMON("공용");

    private final String message;

    public static Gender validate(String name) {
        try {
            return Gender.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new NotExistsException("존재하지 않는 성별입니다");
        }
    }
}
