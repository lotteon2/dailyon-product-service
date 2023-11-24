package com.dailyon.productservice.repository.describeimage;

import com.dailyon.productservice.entity.DescribeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescribeImageJpaRepository extends JpaRepository<DescribeImage, Long> {
}
