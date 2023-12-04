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
    Slice<Product> findProductSlice(Long lastId, Long brandId, List<Category> childCategories,
                                    Gender gender, ProductType productType);

    Page<Product> findProductPage(Long brandId, Long categoryId, ProductType type, Pageable pageable);

    Slice<Product> searchProducts(Long lastId, String query);

    Slice<Product> searchProductsFromOOTD(Long lastId, String query);
}
