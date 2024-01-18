package com.dailyon.productservice.product.repository;

import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductCustomRepository {
    Slice<Product> findProductSlice(
            String lastVal, Long brandId, List<Category> childCategories, Gender gender, ProductType productType,
            Integer lowPrice, Integer highPrice, String query, String sort, String direction
    );

    Page<Product> findProductPage(
            Long brandId, List<Category> childCategories,
            ProductType type, String query, Pageable pageable
    );

    Slice<Product> searchProductsFromOOTD(Long lastId, String query);
}
