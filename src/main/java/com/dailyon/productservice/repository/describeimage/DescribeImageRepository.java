package com.dailyon.productservice.repository.describeimage;

import com.dailyon.productservice.entity.DescribeImage;

import java.util.List;

public interface DescribeImageRepository {
    List<DescribeImage> saveAll(List<DescribeImage> describeImages);
}
