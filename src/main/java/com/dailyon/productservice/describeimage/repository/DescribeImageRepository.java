package com.dailyon.productservice.describeimage.repository;

import com.dailyon.productservice.describeimage.entity.DescribeImage;

import java.util.List;

public interface DescribeImageRepository {
    List<DescribeImage> saveAll(List<DescribeImage> describeImages);
}
