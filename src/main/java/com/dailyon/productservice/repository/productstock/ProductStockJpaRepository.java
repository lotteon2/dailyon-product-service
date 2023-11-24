package com.dailyon.productservice.repository.productstock;

import com.dailyon.productservice.entity.ProductStock;
import com.dailyon.productservice.entity.ids.ProductStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStockJpaRepository extends JpaRepository<ProductStock, ProductStockId> {
}
