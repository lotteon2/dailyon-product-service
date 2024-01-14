package com.dailyon.productservice.describeimage.repository;

import com.dailyon.productservice.describeimage.entity.DescribeImage;
import com.dailyon.productservice.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DescribeImageRepository extends JpaRepository<DescribeImage, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM DescribeImage d WHERE d.product.id = :id")
    void deleteByProductId(@Param("id") Long id);
}
