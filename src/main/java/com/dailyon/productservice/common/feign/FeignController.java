package com.dailyon.productservice.common.feign;

import com.dailyon.productservice.common.feign.request.OrderProductDto;
import com.dailyon.productservice.common.feign.request.ReadWishCartProductRequest;
import com.dailyon.productservice.common.feign.response.ReadOOTDProductListResponse;
import com.dailyon.productservice.common.feign.response.ReadOrderProductListResponse;
import com.dailyon.productservice.common.feign.response.ReadWishCartProductMapResponse;
import com.dailyon.productservice.product.dto.request.CreateProductRequest;
import com.dailyon.productservice.product.dto.response.CreateProductResponse;
import com.dailyon.productservice.product.service.ProductService;
import com.dailyon.productservice.productstock.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
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

    @PostMapping("/auction")
    ResponseEntity<CreateProductResponse> createAuctionProduct(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @DeleteMapping("/auction")
    ResponseEntity<Void> deleteAuctionProduct(@RequestParam List<Long> ids) {
        productService.deleteProductsByIds(ids);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/orders")
    ResponseEntity<ReadOrderProductListResponse> readOrderProducts(@Valid @Size(min = 1) @RequestBody List<OrderProductDto> request) {
        return ResponseEntity.status(HttpStatus.OK).body(productStockService.readOrderProducts(request));
    }

    @PostMapping("/wish-cart")
    ResponseEntity<ReadWishCartProductMapResponse> readWishCartProducts(
            @Valid @Size(min = 1) @RequestBody List<ReadWishCartProductRequest> request) {
        return ResponseEntity.status(HttpStatus.OK).body(productStockService.readWishCartProducts(request));
    }
}
