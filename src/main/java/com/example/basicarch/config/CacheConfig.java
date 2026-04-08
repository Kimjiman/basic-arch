package com.example.basicarch.config;

import com.example.basicarch.base.cache.CacheEventPublisher;
import com.example.basicarch.base.cache.kafka.KafkaCacheEventPublisher;
import com.example.basicarch.base.cache.redis.RedisCacheEventPublisher;
import com.example.basicarch.base.cache.spring.SpringCacheEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * packageName    : com.example.basicarch.config
 * fileName       : CacheConfig
 * author         : KIM JIMAN
 * date           : 26. 3. 17. 화요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 17.     KIM JIMAN      First Commit
 */
@Configuration
public class CacheConfig {
    @Bean
    @ConditionalOnProperty(name = "cache.publisher", havingValue = "redis")
    public CacheEventPublisher redisPublisher(StringRedisTemplate stringRedisTemplate) {
        return new RedisCacheEventPublisher(stringRedisTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "cache.publisher", havingValue = "spring")
    public CacheEventPublisher springPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new SpringCacheEventPublisher(applicationEventPublisher);
    }

    @Bean
    @ConditionalOnProperty(name = "cache.publisher", havingValue = "kafka")
    public CacheEventPublisher kafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaCacheEventPublisher(kafkaTemplate);
    }
}
