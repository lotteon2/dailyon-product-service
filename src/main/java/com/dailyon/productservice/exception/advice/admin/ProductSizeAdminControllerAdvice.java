package com.dailyon.productservice.exception.advice.admin;

import com.dailyon.productservice.controller.admin.ProductSizeAdminController;
import com.dailyon.productservice.exception.ErrorResponse;
import com.dailyon.productservice.exception.NotExistsException;
import com.dailyon.productservice.exception.UniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = ProductSizeAdminController.class)
public class ProductSizeAdminControllerAdvice {
    @ExceptionHandler(NotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse masterCategoryNotExistsException(NotExistsException e) {
        return ErrorResponse.builder()
                .message("존재하지 않는 카테고리입니다")
                .build();
    }

    @ExceptionHandler(UniqueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse duplicatedCategoryException(UniqueException e) {
        return ErrorResponse.builder()
                .message("이미 존재하는 치수값입니다")
                .build();
    }
}
