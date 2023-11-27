package com.dailyon.productservice.product.repository;

import static com.dailyon.productservice.brand.entity.QBrand.brand;
import static com.dailyon.productservice.category.entity.QCategory.category;
import static com.dailyon.productservice.product.entity.QProduct.product;
import static com.dailyon.productservice.reviewaggregate.entity.QReviewAggregate.reviewAggregate;
import static com.dailyon.productservice.describeimage.entity.QDescribeImage.describeImage;

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

    // TODO : no-offset
    @Override
    public Slice<Product> findProductSlice(Long brandId, Long categoryId, Gender gender, ProductType productType, String query, Pageable pageable) {
        JPAQuery<Product> jpaQuery = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.reviewAggregate, reviewAggregate).fetchJoin()
                .where(
                        product.deleted.eq(false),
                        brandIdEq(brandId),
                        categoryIdEq(categoryId),
                        genderEq(gender),
                        productTypeEq(productType),
                        nameEq(query)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        List<Product> result = jpaQuery.fetch();

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
                        categoryIdEq(categoryId),
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
                        categoryIdEq(categoryId),
                        productTypeEq(type)
                )
                .fetchOne();

        return new PageImpl<>(jpaQuery.fetch(), pageable, total);
    }

    private BooleanExpression brandIdEq(Long brandId) {
        return brandId == null ? null : brand.id.eq(brandId);
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId == null ? null : category.id.eq(categoryId);
    }

    private BooleanExpression genderEq(Gender gender) {
        return gender == null ? null : product.gender.eq(gender);
    }

    private BooleanExpression productTypeEq(ProductType productType) {
        return productType == null ? null : product.type.eq(productType);
    }

    private BooleanExpression nameEq(String query) {
        return query == null ? null : product.name.eq(query);
    }
}
