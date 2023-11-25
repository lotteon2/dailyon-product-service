package com.dailyon.productservice.describeimage.repository;

import com.dailyon.productservice.describeimage.entity.DescribeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescribeImageJpaRepository extends JpaRepository<DescribeImage, Long> {
}
