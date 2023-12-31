package com.dailyon.productservice.common.enums;

import com.dailyon.productservice.common.exception.NotExistsException;
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
            throw new NotExistsException(NotExistsException.GENDER_NOT_FOUND);
        }
    }
}
