package com.dailyon.productservice.describeimage.repository;

import com.dailyon.productservice.describeimage.entity.DescribeImage;
import com.dailyon.productservice.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DescribeImageRepositoryImpl implements DescribeImageRepository {
    private final DescribeImageJpaRepository describeImageJpaRepository;

    @Override
    public List<DescribeImage> saveAll(List<DescribeImage> describeImages) {
        return describeImageJpaRepository.saveAll(describeImages);
    }

    @Override
    public void deleteByProduct(Product product) {
        describeImageJpaRepository.deleteByProduct(product);
    }
}
