package com.dailyon.productservice.productstock.repository;

import com.dailyon.productservice.common.feign.request.OrderProductDto;
import com.dailyon.productservice.productstock.entity.ProductStock;
import com.dailyon.productservice.productstock.kafka.dto.OrderDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

import static com.dailyon.productservice.product.entity.QProduct.product;
import static com.dailyon.productservice.productsize.entity.QProductSize.productSize;
import static com.dailyon.productservice.productstock.entity.QProductStock.productStock;

@Repository
@RequiredArgsConstructor
public class ProductStockCustomRepositoryImpl implements ProductStockCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ProductStock> findOrderProductsBy(List<OrderProductDto> productDtos) {
        BooleanBuilder builder = new BooleanBuilder();
        for(OrderProductDto dto: productDtos) {
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

    @Override
    public List<ProductStock> selectProductStocksForUpdate(List<OrderDto.ProductInfo> productInfos) {
        BooleanBuilder builder = new BooleanBuilder();
        for(OrderDto.ProductInfo dto: productInfos) {
            builder.or(
                    (productStock.product.id.eq(dto.getProductId())
                            .and(productStock.productSize.id.eq(dto.getSizeId()))
                            .and(productStock.productSize.deleted.isFalse())
                            .and(productStock.product.deleted.isFalse())
                    )
            );
        }

        return jpaQueryFactory.selectFrom(productStock)
                .where(builder)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetch();
    }
}
