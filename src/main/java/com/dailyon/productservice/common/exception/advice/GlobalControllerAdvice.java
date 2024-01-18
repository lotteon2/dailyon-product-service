package com.dailyon.productservice.common.exception.advice;

import com.dailyon.productservice.common.exception.DeleteException;
import com.dailyon.productservice.common.exception.ErrorResponse;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.exception.UniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validException(BindException e) {
        return ErrorResponse.builder()
                .message(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage())
                .build();
    }

    @ExceptionHandler(NotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notExistsException(NotExistsException e) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(UniqueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse duplicatedCategoryException(UniqueException e) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(DeleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse cannotDeleteException(DeleteException e) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .build();
    }
}
