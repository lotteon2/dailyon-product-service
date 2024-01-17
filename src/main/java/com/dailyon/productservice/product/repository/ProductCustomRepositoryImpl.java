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
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Product> findProductSlice(
            String lastVal, Long brandId, List<Category> childCategories, Gender gender, ProductType productType,
            Integer lowPrice, Integer highPrice, String sort, String direction
    ) {
        Pageable pageable = Pageable.ofSize(8);

        List<Product> idx = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.reviewAggregate, reviewAggregate).fetchJoin()
                .where(product.deleted.eq(false)
                        .and(brandIdEq(brandId))
                        .and(categoryIn(childCategories))
                        .and(genderEq(gender))
                        .and(productTypeEq(productType))
                        .and(filterPrice(lowPrice, highPrice))
                        .and(filterByLastVal(sort, direction, lastVal))
                ).fetch();

        List<Product> result = jpaQueryFactory
                .select(product)
                .from(product)
                .where(product.in(idx))
                .orderBy(orderSpecifier(sort, direction))
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
    public Page<Product> findProductPage(Long brandId, List<Category> childCategories, ProductType type, Pageable pageable) {
        List<Product> idx = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.describeImages, describeImage).fetchJoin()
                .where(product.deleted.eq(false)
                        .and(brandIdEq(brandId))
                        .and(categoryIn(childCategories))
                        .and(productTypeEq(type))
                ).fetch();

        List<Product> result = jpaQueryFactory
                .select(product)
                .from(product)
                .where(product.in(idx))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = idx.size();

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Slice<Product> searchProducts(Long lastId, String query) {
        Pageable pageable = Pageable.ofSize(8);

        List<Product> idx = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.reviewAggregate, reviewAggregate).fetchJoin()
                .where(product.deleted.eq(false)
                        .and(product.id.gt(lastId))
                        .and(nameContains(query).or(codeEq(query)))
                        .and(productTypeEq(ProductType.NORMAL))
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
    public Slice<Product> searchProductsFromOOTD(Long lastId, String query) {
        Pageable pageable = Pageable.ofSize(8);

        List<Product> idx = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.productStocks, productStock).fetchJoin()
                .where(product.deleted.eq(false)
                        .and(product.id.gt(lastId))
                        .and(nameContains(query).or(codeEq(query)))
                        .and(productTypeEq(ProductType.NORMAL))
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

    private BooleanExpression filterPrice(Integer lowPrice, Integer highPrice) {
        if(lowPrice == null && highPrice == null) {
            return null;
        } else if(lowPrice == null && highPrice != null) {
            return product.price.loe(highPrice);
        } else if(lowPrice != null && highPrice == null) {
            return product.price.goe(lowPrice);
        } else {
            return product.price.between(lowPrice, highPrice);
        }
    }

    private OrderSpecifier<?> orderSpecifier(String sort, String direction) {
        if("price".equals(sort)) {
            return "asc".equals(direction) ?
                    product.price.asc() :
                    product.price.desc();
        } else if("rating".equals(sort)) {
            return "asc".equals(direction) ?
                    product.reviewAggregate.avgRating.asc() :
                    product.reviewAggregate.avgRating.desc();
        } else if("review".equals(sort)) {
            return "asc".equals(direction) ?
                    product.reviewAggregate.reviewCount.asc() :
                    product.reviewAggregate.reviewCount.desc();
        } else { // 기본은 최신순 내림차순
            return product.createdAt.desc();
        }
    }

    private BooleanExpression filterByLastVal(String sort, String direction, String lastVal) {
        if ("price".equals(sort)) {
            return "asc".equals(direction) ?
                    product.price.gt(Integer.parseInt(lastVal)) :
                    product.price.lt(Integer.parseInt(lastVal));
        } else if ("rating".equals(sort)) {
            return "asc".equals(direction) ?
                    product.reviewAggregate.avgRating.gt(Double.parseDouble(lastVal)) :
                    product.reviewAggregate.avgRating.lt(Double.parseDouble(lastVal));
        } else if("review".equals(sort)) {
            return "asc".equals(direction) ?
                    product.reviewAggregate.reviewCount.gt(Long.parseLong(lastVal)) :
                    product.reviewAggregate.reviewCount.lt(Long.parseLong(lastVal));
        } else {
            return product.createdAt.lt(lastVal != null ?
                    LocalDateTime.parse(lastVal, DateTimeFormatter.ISO_LOCAL_DATE_TIME) :
                    LocalDateTime.now()
            );
        }
    }
}
