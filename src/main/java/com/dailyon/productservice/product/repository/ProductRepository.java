package com.dailyon.productservice.product.repository;

import com.dailyon.productservice.brand.entity.Brand;
import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {
    @Query(value = "SELECT p " +
            "FROM Product p " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.brand " + 
            "LEFT JOIN FETCH p.reviewAggregate " +
            "LEFT JOIN FETCH p.productStocks " +
            "LEFT JOIN FETCH p.describeImages " +
            "WHERE p.id = :id AND p.deleted = false")
    Optional<Product> findProductDetailById(@Param("id") Long id);
    Optional<Product> findProductByCode(String code);
    @Query(value = "SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.brand " +
            "WHERE p.id IN :ids AND p.deleted = false")
    List<Product> findOOTDProductDetails(@Param("ids") List<Long> ids);

    boolean existsProductByBrand(Brand brand);

    @Query(value = "SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.category " +
            "WHERE p.category IN :categories AND p.deleted = false")
    List<Product> findProductsFromCategories(@Param("categories") List<Category> categories);

    @Query(value = "SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.category " +
            "JOIN FETCH p.brand " +
            "WHERE p.type = 'NORMAL' AND p.deleted = false " +
            "ORDER BY p.createdAt DESC")
    List<Product> findNewProducts(Pageable pageable);

    @Query(value = "SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.category " +
            "JOIN FETCH p.brand " +
            "WHERE p.type = 'NORMAL' AND p.deleted = false AND p.id IN :ids"
    )
    List<Product> findBestProducts(@Param("ids") List<Long> ids);
}
