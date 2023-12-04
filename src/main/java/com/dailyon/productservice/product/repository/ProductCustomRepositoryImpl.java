package com.dailyon.productservice.product.repository;

import static com.dailyon.productservice.brand.entity.QBrand.brand;
import static com.dailyon.productservice.category.entity.QCategory.category;
import static com.dailyon.productservice.product.entity.QProduct.product;
import static com.dailyon.productservice.reviewaggregate.entity.QReviewAggregate.reviewAggregate;
import static com.dailyon.productservice.describeimage.entity.QDescribeImage.describeImage;
import static com.dailyon.productservice.productstock.entity.QProductStock.productStock;

import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.product.entity.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Product> findProductSlice(Long lastId, Long brandId, List<Category> childCategories, Gender gender, ProductType productType) {
        Pageable pageable = Pageable.ofSize(8);

        List<Product> idx = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.reviewAggregate, reviewAggregate).fetchJoin()
                .where(
                        product.deleted.eq(false),
                        product.id.gt(lastId),
                        brandIdEq(brandId),
                        categoryIn(childCategories),
                        genderEq(gender),
                        productTypeEq(productType)
                ).fetch();

        List<Product> result = jpaQueryFactory
                .select(product)
                .from(product)
                .where(product.in(idx))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if(result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    @Override
    public Page<Product> findProductPage(Long brandId, Long categoryId, ProductType type, Pageable pageable) {
        JPAQuery<Product> jpaQuery = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.describeImages, describeImage).fetchJoin()
                .where(
                        product.deleted.eq(false),
                        brandIdEq(brandId),
                        productTypeEq(type)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Long total = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(
                        product.deleted.eq(false),
                        brandIdEq(brandId),
                        productTypeEq(type)
                )
                .fetchOne();

        return new PageImpl<>(jpaQuery.fetch(), pageable, total);
    }

    @Override
    public Slice<Product> searchProducts(Long lastId, String query, String code) {
        Pageable pageable = Pageable.ofSize(8);

        List<Product> idx = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.reviewAggregate, reviewAggregate).fetchJoin()
                .where(
                        product.deleted.eq(false),
                        product.id.gt(lastId),
                        nameContains(query),
                        codeEq(code),
                        productTypeEq(ProductType.NORMAL)
                ).fetch();

        List<Product> result = jpaQueryFactory
                .select(product)
                .from(product)
                .where(product.in(idx))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if(result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    @Override
    public Slice<Product> searchProductsFromOOTD(Long lastId, String query, String code) {
        Pageable pageable = Pageable.ofSize(8);

        List<Product> idx = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.productStocks, productStock).fetchJoin()
                .where(
                        product.deleted.eq(false),
                        product.id.gt(lastId),
                        nameContains(query),
                        codeEq(code),
                        productTypeEq(ProductType.NORMAL)
                ).fetch();

        List<Product> result = jpaQueryFactory
                .select(product)
                .from(product)
                .where(product.in(idx))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if(result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    private BooleanExpression brandIdEq(Long brandId) {
        return brandId == null ? null : brand.id.eq(brandId);
    }

    private BooleanExpression categoryIn(List<Category> childCategories) {
        return childCategories == null ? null : category.in(childCategories);
    }

    private BooleanExpression genderEq(Gender gender) {
        return gender == null ? null : product.gender.eq(gender);
    }

    private BooleanExpression productTypeEq(ProductType productType) {
        return productType == null ? null : product.type.eq(productType);
    }

    private BooleanExpression nameContains(String name) {
        return name == null ? null : product.name.contains(name);
    }

    private BooleanExpression codeEq(String code) {
        return code == null ? null : product.code.eq(code);
    }
}
