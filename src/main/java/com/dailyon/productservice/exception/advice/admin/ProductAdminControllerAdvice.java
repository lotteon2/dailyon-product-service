package com.dailyon.productservice.exception.advice.admin;

import com.amazonaws.services.kms.model.NotFoundException;
import com.dailyon.productservice.controller.admin.ProductAdminController;
import com.dailyon.productservice.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = ProductAdminController.class)
public class ProductAdminControllerAdvice {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notExistsException(NotFoundException e) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .build();
    }
}
