package com.dailyon.productservice.product.cache;

import com.dailyon.productservice.product.entity.Product;
import com.dailyon.productservice.product.vo.NewProductVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NewProductCacheRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

//    @CachePut(value = "newProducts", key = "#productId", unless = "#result == null")
    public NewProductVO putNewProductCache(Long productId, Product product) throws JsonProcessingException {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        NewProductVO newProductVO = NewProductVO.fromEntity(product);

        // 신상품 캐시 저장, 생성 시점을 score로 sorted set
        hashOps.put("newProducts", String.valueOf(productId), objectMapper.writeValueAsString(newProductVO));
        zSetOps.add("newProductsOrder", String.valueOf(productId), System.currentTimeMillis());

        // 신상품 캐시는 최대 100개이므로 100개 초과 시 가장 오래된 캐시 삭제
        if(zSetOps.zCard("newProductsOrder") > 100) {
            Set<String> productIds = zSetOps.range("newProductsOrder", 0, 0);
            if(productIds != null && !productIds.isEmpty()) {
                String oldestProductId = productIds.iterator().next();

                hashOps.delete("newProducts", oldestProductId);
                zSetOps.remove("newProductsOrder", oldestProductId);
            }
        }

        return newProductVO;
    }

//    @Cacheable(value = "newProducts", unless = "#result == null")
    public List<NewProductVO> readNewProductCache() {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

        Collection<String> newProduct = hashOps.entries("newProducts").values();

        return newProduct.stream().map(product -> {
            try {
                return objectMapper.readValue(product, NewProductVO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).collect(Collectors.toList());
    }

//    @CacheEvict(value = "newProducts", key = "#productId")
    public void deleteNewProductCache(Long productId) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        hashOps.delete("newProducts", String.valueOf(productId));
        zSetOps.remove("newProductsOrder", productId);
    }
}
