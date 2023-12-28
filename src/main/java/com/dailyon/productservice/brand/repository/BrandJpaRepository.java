package com.dailyon.productservice.brand.repository;

import com.dailyon.productservice.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandJpaRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);
}
