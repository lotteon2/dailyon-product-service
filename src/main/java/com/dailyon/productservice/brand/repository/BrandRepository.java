package com.dailyon.productservice.brand.repository;

import com.dailyon.productservice.brand.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);
    List<Brand> findBrandsByNameContainsOrderByName(String name);
    Page<Brand> findAllByOrderByNameAsc(Pageable pageable);
    List<Brand> findAllByOrderByNameAsc();
}
