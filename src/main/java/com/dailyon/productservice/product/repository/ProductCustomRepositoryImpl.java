package com.dailyon.productservice.product.repository;

import com.dailyon.productservice.category.entity.Category;
import com.dailyon.productservice.common.enums.Gender;
import com.dailyon.productservice.common.enums.ProductType;
import com.dailyon.productservice.product.entity.Product;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.dailyon.productservice.brand.entity.QBrand.brand;
import static com.dailyon.productservice.category.entity.QCategory.category;
import static com.dailyon.productservice.describeimage.entity.QDescribeImage.describeImage;
import static com.dailyon.productservice.product.entity.QProduct.product;
import static com.dailyon.productservice.productstock.entity.QProductStock.productStock;
import static com.dailyon.productservice.reviewaggregate.entity.QReviewAggregate.reviewAggregate;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Product> findProductSlice(
            Long brandId, List<Category> childCategories, Gender gender, ProductType productType,
            Integer lowPrice, Integer highPrice, String query, int page, String sort, String direction
    ) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(query != null) {
            booleanBuilder.and(nameContains(query).or(codeContains(query)));
        }
        Pageable pageable = PageRequest.of(page, 8);

        List<Long> indexes = jpaQueryFactory
                .select(product.id)
                .from(product)
                .where(product.deleted.eq(false)
                        .and(brandIdEq(brandId))
                        .and(categoryIn(childCategories))
                        .and(genderEq(gender))
                        .and(productTypeEq(productType))
                        .and(filterPrice(lowPrice, highPrice))
                        .and(booleanBuilder)
                )
                .orderBy(orderSpecifier(sort, direction))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        if(indexes.isEmpty()) {
            return new SliceImpl<>(new ArrayList<>(), pageable, false);
        }

        List<Product> result = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.reviewAggregate, reviewAggregate).fetchJoin()
                .where(
                        product.id.in(indexes)
                )
                .orderBy(orderSpecifier(sort, direction))
                .fetch();

        boolean hasNext = false;
        if(result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    @Override
    public Page<Product> findProductPage(
            Long brandId, List<Category> childCategories, ProductType type, String query,
            int page, int size, String sort, String direction
    ) {
        Pageable pageable = PageRequest.of(page, size);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(query != null) {
            booleanBuilder.and(nameContains(query).or(codeContains(query)));
        }
        JPAQuery<Long> indexQuery = jpaQueryFactory
                .select(product.id)
                .from(product)
                .where(product.deleted.eq(false)
                        .and(brandIdEq(brandId))
                        .and(categoryIn(childCategories))
                        .and(booleanBuilder)
                        .and(productTypeEq(type))
                )
                .orderBy(orderSpecifier(sort, direction))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Long> indexes = indexQuery.fetch();
        if(indexes.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        JPAQuery<Product> resultQuery = jpaQueryFactory
                .selectDistinct(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.describeImages, describeImage).fetchJoin()
                .leftJoin(product.productStocks, productStock).fetchJoin()
                .leftJoin(product.reviewAggregate, reviewAggregate).fetchJoin()
                .orderBy(orderSpecifier(sort, direction))
                .where(product.id.in(indexes));

        List<Product> result = resultQuery.fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(product.id)
                .from(product)
                .where(product.deleted.eq(false)
                        .and(brandIdEq(brandId))
                        .and(categoryIn(childCategories))
                        .and(booleanBuilder)
                        .and(productTypeEq(type))
                );

        long total = countQuery.fetchCount();
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Slice<Product> searchProductsFromOOTD(Long lastId, String query) {
        Pageable pageable = Pageable.ofSize(8);
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(query != null) {
            booleanBuilder.and(nameContains(query).or(codeContains(query)));
        }

        List<Product> result = jpaQueryFactory
                .select(product)
                .from(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.productStocks, productStock).fetchJoin()
                .where(product.deleted.eq(false)
                        .and(product.id.gt(lastId))
                        .and(booleanBuilder)
                        .and(productTypeEq(ProductType.NORMAL))
                )
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
    public List<Product> searchProducts(String query) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(query != null) {
            booleanBuilder.and(nameContains(query).or(codeContains(query)));
        }

        return jpaQueryFactory.selectFrom(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.reviewAggregate, reviewAggregate).fetchJoin()
                .where(product.deleted.eq(false)
                        .and(booleanBuilder)
                        .and(productTypeEq(ProductType.NORMAL))
                )
                .orderBy(orderSpecifier("createdAt", "desc"))
                .fetch();
    }

    @Override
    public List<Product> searchAfterGpt(List<Long> categoryIds, List<Long> brandIds, Gender gender) {
        return jpaQueryFactory.selectFrom(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .leftJoin(product.reviewAggregate, reviewAggregate).fetchJoin()
                .where(product.brand.id.in(brandIds)
                        .and(product.category.id.in(categoryIds))
                        .and(product.type.eq(ProductType.NORMAL))
                        .and(product.gender.eq(gender))
                )
                .orderBy(orderSpecifier("createdAt", "desc"))
                .fetch();
    }

    private BooleanExpression brandIdEq(Long brandId) {
        return brandId == null ? null : product.brand.id.eq(brandId);
    }

    private BooleanExpression categoryIn(List<Category> childCategories) {
        return childCategories == null ? null : product.category.in(childCategories);
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

    private BooleanExpression codeContains(String code) {
        return code == null ? null : product.code.contains(code);
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
}
