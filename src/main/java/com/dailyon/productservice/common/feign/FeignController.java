package com.dailyon.productservice.common.feign;

import com.dailyon.productservice.product.dto.request.ReadOrderProductRequest;
import com.dailyon.productservice.product.dto.response.ReadOOTDProductListResponse;
import com.dailyon.productservice.product.dto.response.ReadOrderProductListResponse;
import com.dailyon.productservice.product.service.ProductService;
import com.dailyon.productservice.productstock.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clients/products")
@RequiredArgsConstructor
public class FeignController {
    private final ProductService productService;
    private final ProductStockService productStockService;

    @GetMapping("/post-image")
    ResponseEntity<ReadOOTDProductListResponse> readOOTDProductDetail(@RequestParam List<Long> id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.readOOTDProductDetails(id));
    }

    @PostMapping("/orders")
    ResponseEntity<ReadOrderProductListResponse> readOrderProducts(@Valid
                                                                   @RequestBody ReadOrderProductRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(productStockService.readOrderProducts(request));
    }
}
