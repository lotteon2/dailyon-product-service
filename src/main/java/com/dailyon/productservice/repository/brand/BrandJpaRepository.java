package com.dailyon.productservice.repository.brand;

import com.dailyon.productservice.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandJpaRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);
    List<Brand> findAllByDeletedIsFalse();
    Optional<Brand> findByIdAndDeletedIsFalse(Long id);
}
