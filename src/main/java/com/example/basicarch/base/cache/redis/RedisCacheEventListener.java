package com.example.basicarch.base.cache.redis;

import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.service.BaseCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.basicarch.base.cache
 * fileName       : RedisCacheEventListener
 * author         : KIM JIMAN
 * date           : 26. 3. 27. 금요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 27.     KIM JIMAN      First Commit
 * Redis Pub/Sub 채널을 구독하다가 메시지가 오면 해당 캐시를 삭제한다.
 * 멀티 서버 + Caffeine 로컬 캐시 환경에서 사용.
 */
@Slf4j
@Component
public class RedisCacheEventListener implements MessageListener {
    private final Map<CacheType, BaseCacheService> handlerMap;

    public RedisCacheEventListener(List<BaseCacheService> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(BaseCacheService::getCacheType, it -> it));
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());
        log.info("[RedisCacheEventListener] onMessage body: {}", body);
        try {
            CacheType cacheType = CacheType.fromValue(body);
            onMessage(cacheType);
        } catch (IllegalArgumentException e) {
            log.error("[RedisCacheEventListener] Unknown cache type: {}", body);
        }
    }

    public void onMessage(CacheType cacheType) {
        log.info("[RedisCacheEventListener] onMessage cacheType: {}", cacheType);
        BaseCacheService cacheHandler = handlerMap.get(cacheType);
        if (cacheHandler != null) {
            cacheHandler.evict();
        }
    }
}
