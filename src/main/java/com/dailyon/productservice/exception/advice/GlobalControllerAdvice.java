package com.dailyon.productservice.exception.advice;

import com.dailyon.productservice.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validException(BindException e) {
        return ErrorResponse.builder()
                .message(e.getBindingResult().getFieldError().getDefaultMessage())
                .build();
    }
}
