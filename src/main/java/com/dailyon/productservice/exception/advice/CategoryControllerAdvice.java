package com.dailyon.productservice.exception.advice;

import com.dailyon.productservice.controller.CategoryController;
import com.dailyon.productservice.exception.ErrorResponse;
import com.dailyon.productservice.exception.NotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = CategoryController.class)
public class CategoryControllerAdvice {

    @ExceptionHandler(NotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse categoryNotExistsException(NotExistsException e) {
        return ErrorResponse.builder()
                .message("존재하지 않는 카테고리입니다")
                .build();
    }

}
