package com.dailyon.productservice.productstock.repository;

import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.productstock.entity.ProductStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductStockRepositoryImpl implements ProductStockRepository {
    private final ProductStockJpaRepository productStockJpaRepository;

    @Override
    public List<ProductStock> saveAll(List<ProductStock> productStocks) {
        return productStockJpaRepository.saveAll(productStocks);
    }

    @Override
    public List<ProductStock> findProductsByProduct(Product product) {
        return productStockJpaRepository.findProductStocksByProductOrderByProductSize(product);
    }
}
