package com.dailyon.productservice.product.repository;

import com.dailyon.productservice.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {
    @Query(value = "SELECT p " +
        "FROM Product p " +
        "JOIN FETCH p.brand " +
        "JOIN FETCH p.reviewAggregate " +
        "JOIN FETCH p.productStocks " +
        "JOIN FETCH p.describeImages " +
        "WHERE p.id = :id AND p.deleted = false")
    Optional<Product> findProductDetailById(Long id);
    Optional<Product> findByIdAndDeletedIsFalse(Long id);
    Optional<Product> findProductByCode(String code);
}
