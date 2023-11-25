package com.dailyon.productservice.common.exception;

public class NotExistsException extends RuntimeException {
    public static final String CATEGORY_NOT_FOUND = "존재하지 않는 카테고리입니다";
    public static final String BRAND_NOT_FOUND = "존재하지 않는 브랜드입니다";
    public static final String PRODUCT_SIZE_NOT_FOUND = "존재하지 않는 치수값입니다";
    public static final String GENDER_NOT_FOUND = "존재하지 않는 성별입니다";
    public static final String PRODUCT_TYPE_NOT_FOUND = "존재하지 않는 상품 유형입니다";
    public static final String PRODUCT_NOT_FOUND = "존재하지 않는 상품입니다";

    public NotExistsException() {
        super();
    }

    public NotExistsException(String message) {
        super(message);
    }

    public NotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistsException(Throwable cause) {
        super(cause);
    }

    protected NotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
