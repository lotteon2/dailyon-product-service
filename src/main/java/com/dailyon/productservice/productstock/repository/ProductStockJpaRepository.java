package com.dailyon.productservice.productstock.repository;

import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.dailyon.productservice.productstock.entity.ProductStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductStockJpaRepository extends JpaRepository<ProductStock, ProductStockId> {
    List<ProductStock> findProductStocksByProductOrderByProductSize(Product product);
}
