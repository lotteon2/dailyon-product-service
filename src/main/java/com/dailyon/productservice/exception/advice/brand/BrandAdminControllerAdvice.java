package com.dailyon.productservice.exception.advice.brand;

import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.exception.UniqueException;
import com.dailyon.productservice.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BrandAdminControllerAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse duplicatedBrandException(UniqueException e) {
        return ErrorResponse.builder()
                .message("이미 존재하는 브랜드 이름입니다")
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse brandNotFoundException(NotExistsException e) {
        return ErrorResponse.builder()
                .message("존재하지 않는 브랜드입니다")
                .build();
    }
}
