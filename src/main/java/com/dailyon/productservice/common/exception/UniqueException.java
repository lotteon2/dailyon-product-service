package com.dailyon.productservice.common.exception;

public class UniqueException extends RuntimeException {
    public static String DUPLICATE_PRODUCT_SIZE_NAME = "이미 존재하는 치수값입니다";
    public static String DUPLICATE_BRAND_NAME = "이미 존재하는 브랜드 이름입니다";
    public static String DUPLICATE_CATEGORY_NAME = "이미 존재하는 카테고리 이름입니다";
    public static String DUPLICATE_PRODUCT_CODE = "이미 존재하는 상품 코드입니다";
    public UniqueException() {
        super();
    }

    public UniqueException(String message) {
        super(message);
    }

    public UniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueException(Throwable cause) {
        super(cause);
    }

    protected UniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
