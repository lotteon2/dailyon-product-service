package com.dailyon.productservice.exception;

public class DuplicatedBrandException extends RuntimeException {
    public DuplicatedBrandException() {
        super();
    }

    public DuplicatedBrandException(String message) {
        super(message);
    }

    public DuplicatedBrandException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedBrandException(Throwable cause) {
        super(cause);
    }

    protected DuplicatedBrandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
