package com.dailyon.productservice.productstock.repository;

import com.dailyon.productservice.product.dto.request.ReadOrderProductRequest;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dailyon.productservice.product.entity.QProduct.product;
import static com.dailyon.productservice.productsize.entity.QProductSize.productSize;
import static com.dailyon.productservice.productstock.entity.QProductStock.productStock;


@Repository
@RequiredArgsConstructor
public class ProductStockCustomRepositoryImpl implements ProductStockCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ProductStock> findOrderProductsBy(List<ReadOrderProductRequest.ProductDto> productDtos) {
        BooleanBuilder builder = new BooleanBuilder();
        for(ReadOrderProductRequest.ProductDto dto: productDtos) {
            builder.or(
                    (productStock.product.id.eq(dto.getProductId())
                        .and(productStock.productSize.id.eq(dto.getSizeId()))
                        .and(productStock.productSize.deleted.isFalse())
                        .and(productStock.product.deleted.isFalse())
                    )
            );
        }

        return jpaQueryFactory.selectFrom(productStock)
                .leftJoin(productStock.product, product).fetchJoin()
                .leftJoin(productStock.productSize, productSize).fetchJoin()
                .where(builder)
                .fetch();
    }
}
