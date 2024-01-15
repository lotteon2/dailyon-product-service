package com.dailyon.productservice.common.config;

import com.dailyon.productservice.product.dto.response.ReadBestProductListResponse;
import com.dailyon.productservice.product.dto.response.ReadNewProductListResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModules(new JavaTimeModule(), new Jdk8Module());
        return objectMapper;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(ObjectMapper objectMapper) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
            ObjectMapper objectMapper
    ) {
        Jackson2JsonRedisSerializer<ReadNewProductListResponse> newProductVOJackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(ReadNewProductListResponse.class);

        Jackson2JsonRedisSerializer<ReadBestProductListResponse> bestProductVOJackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(ReadBestProductListResponse.class);

        newProductVOJackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        bestProductVOJackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return (builder -> builder
                .withCacheConfiguration(
                        "newProducts",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofDays(1))
                                .disableCachingNullValues()
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer())
                                )
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(newProductVOJackson2JsonRedisSerializer)
                                )
                )
                .withCacheConfiguration(
                        "bestProducts",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofDays(7))
                                .disableCachingNullValues()
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer())
                                )
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(bestProductVOJackson2JsonRedisSerializer)
                                )
                )
        );
    }
}
