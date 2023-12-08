package com.dailyon.productservice.product.repository;

import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {
    @Query(value = "SELECT p " +
        "FROM Product p " +
        "JOIN FETCH p.category " +
        "JOIN FETCH p.brand " +
        "JOIN FETCH p.reviewAggregate " +
        "JOIN FETCH p.productStocks " +
        "JOIN FETCH p.describeImages " +
        "WHERE p.id = :id AND p.deleted = false")
    Optional<Product> findProductDetailById(Long id);
    Optional<Product> findProductByCode(String code);
    @Query(value = "SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.brand " +
            "WHERE p.id IN :id AND p.deleted = false")
    List<Product> findOOTDProductDetails(List<Long> id);

    boolean existsProductByBrand(Brand brand);

    @Query(value = "SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.category " +
            "WHERE p.category IN :categories AND p.deleted = false")
    List<Product> findProductsFromCategories(List<Category> categories);
}
