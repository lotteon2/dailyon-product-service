package com.dailyon.productservice.common.exception;

public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException() {
        super();
    }

    public InsufficientQuantityException(String message) {
        super(message);
    }

    public InsufficientQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientQuantityException(Throwable cause) {
        super(cause);
    }

    protected InsufficientQuantityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
