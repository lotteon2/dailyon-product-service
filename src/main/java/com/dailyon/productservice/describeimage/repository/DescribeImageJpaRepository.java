package com.dailyon.productservice.describeimage.repository;

import com.dailyon.productservice.describeimage.entity.DescribeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DescribeImageJpaRepository extends JpaRepository<DescribeImage, Long> {
    @Modifying
    @Query(value = "DELETE FROM DescribeImage d WHERE d.product.id = :id")
    void deleteByProductId(Long id);
}
