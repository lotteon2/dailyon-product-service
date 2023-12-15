package com.dailyon.productservice.productstock.repository;

import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.dailyon.productservice.productstock.entity.ProductStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, ProductStockId>, ProductStockCustomRepository {

    List<ProductStock> findProductStocksByProductOrderByProductSize(Product product);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM ProductStock ps WHERE ps.product.id = :productId")
    void deleteByProductId(Long productId);
}
