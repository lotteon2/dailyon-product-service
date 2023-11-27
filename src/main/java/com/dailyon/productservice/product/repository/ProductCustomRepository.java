package com.dailyon.productservice.product.repository;

import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductCustomRepository {
    Slice<Product> findProductSlice(Long brandId, Long categoryId,
                                    Gender gender, ProductType productType,
                                    String query, Pageable pageable);
}
