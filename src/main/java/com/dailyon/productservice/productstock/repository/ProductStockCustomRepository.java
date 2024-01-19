package com.dailyon.productservice.productstock.repository;

import com.dailyon.productservice.common.feign.request.OrderProductDto;
import com.dailyon.productservice.common.feign.request.ReadWishCartProductRequest;
import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.dailyon.productservice.productstock.kafka.dto.OrderDto;

import java.util.List;


public interface ProductStockCustomRepository {

    List<ProductStock> findOrderProductsBy(List<OrderProductDto> productDtos);

    List<ProductStock> selectProductStocksForUpdate(List<OrderDto.ProductInfo> productInfos);

    List<ProductStock> findWishCartProductsBy(List<ReadWishCartProductRequest> requests);

    List<ProductStock> findProductStocksByProductOrderByProductSize(Product product);
}
