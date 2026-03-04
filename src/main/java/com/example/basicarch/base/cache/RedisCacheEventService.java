package com.example.basicarch.base.cache;

import com.example.basicarch.base.constants.CacheType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCacheEventService {
    private static final String CHANNEL = "cache:invalidate";
    private final StringRedisTemplate redisTemplate;

    public void save(String key, String field, String json) {
        redisTemplate.delete(key);
        redisTemplate.opsForHash().put(key, field, json);
    }

    public void saveAll(String key, Map<String, String> data) {
        redisTemplate.delete(key);
        redisTemplate.opsForHash().putAll(key, data);
    }

    public Optional<String> get(String key, String field) {
        Object value = redisTemplate.opsForHash().get(key, field);
        return Optional.ofNullable(value).map(Object::toString);
    }

    public Map<Object, Object> entries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void publishEvent(CacheType cacheType) {
        redisTemplate.convertAndSend(CHANNEL, cacheType.getValue());
    }
}
