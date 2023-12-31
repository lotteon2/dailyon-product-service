package com.dailyon.productservice.common.exception;

public class DeleteException extends RuntimeException {
    public static final String BRAND_PRODUCT_EXISTS = "브랜드에 상품이 존재합니다";
    public static final String CATEGORY_PRODUCT_EXISTS = "카테고리에 상품이 존재합니다";
    public static final String COUPON_EXISTS = "번 id 상품에 적용 가능한 쿠폰이 존재합니다";
    public DeleteException() {
        super();
    }

    public DeleteException(String message) {
        super(message);
    }

    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteException(Throwable cause) {
        super(cause);
    }

    protected DeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
