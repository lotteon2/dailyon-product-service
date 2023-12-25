package com.dailyon.productservice.productstock.service;

import com.dailyon.productservice.common.exception.InsufficientQuantityException;
import com.dailyon.productservice.common.exception.NotExistsException;
import com.dailyon.productservice.common.feign.request.OrderProductDto;
import com.dailyon.productservice.common.feign.request.ReadWishCartProductRequest;
import com.dailyon.productservice.common.feign.response.ReadOrderProductListResponse;
import com.dailyon.productservice.common.feign.response.ReadWishCartProductMapResponse;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.dailyon.productservice.productstock.kafka.dto.OrderDto;
import com.dailyon.productservice.productstock.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductStockService {
    private final ProductStockRepository productStockRepository;

    public ReadOrderProductListResponse readOrderProducts(List<OrderProductDto> request) {
        return ReadOrderProductListResponse.fromEntity(productStockRepository.findOrderProductsBy(request));
    }

    public ReadWishCartProductMapResponse readWishCartProducts(List<ReadWishCartProductRequest> requests) {
        return ReadWishCartProductMapResponse.fromEntity(productStockRepository.findWishCartProductsBy(requests));
    }

    @Transactional
    public void deductProductStocks(List<OrderDto.ProductInfo> productInfos) {
        List<ProductStock> productStocks = productStockRepository.selectProductStocksForUpdate(productInfos);
        if(productStocks.size() != productInfos.size()) {
            throw new NotExistsException(NotExistsException.PRODUCT_NOT_FOUND);
        }
        // 둘은 같은 정렬 기준을 공유.
        Collections.sort(productStocks);
        Collections.sort(productInfos);

        for(int i=0; i<productStocks.size(); i++) {
            long quantity = productStocks.get(i).getQuantity() - productInfos.get(i).getQuantity();
            if(quantity < 0) {
                throw new InsufficientQuantityException("재고가 부족합니다");
            }
            productStocks.get(i).setQuantity(quantity);
        }
    }

    @Transactional
    public void addProductStocks(List<OrderDto.ProductInfo> productInfos) {
        List<ProductStock> productStocks = productStockRepository.selectProductStocksForUpdate(productInfos);
        if(productStocks.size() != productInfos.size()) {
            throw new NotExistsException(NotExistsException.PRODUCT_NOT_FOUND);
        }
        // 둘은 같은 정렬 기준을 공유.
        Collections.sort(productStocks);
        Collections.sort(productInfos);

        for(int i=0; i<productStocks.size(); i++) {
            long quantity = productStocks.get(i).getQuantity() + productInfos.get(i).getQuantity();
            productStocks.get(i).setQuantity(quantity);
        }
    }
}
