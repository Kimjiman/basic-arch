package com.example.basicarch.base.cache.spring;

import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.service.BaseCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.basicarch.base.cache
 * fileName       : SpringCacheEventListener
 * author         : KIM JIMAN
 * date           : 26. 3. 27. 금요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 27.     KIM JIMAN      First Commit
 * CacheInvalidateEvent를 수신하면 해당 캐시를 삭제한다.
 * 단일 서버 환경에서 Spring 이벤트로 Caffeine 캐시 무효화.
 */
@Slf4j
@Component
public class SpringCacheEventListener {
    private final Map<CacheType, BaseCacheService> handlerMap;

    public SpringCacheEventListener(List<BaseCacheService> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(BaseCacheService::getCacheType, it -> it));
    }

    @EventListener
    public void handleEvent(SpringCacheInvalidateEvent event) {
        onMessage(event.cacheType());
    }

    public void onMessage(CacheType cacheType) {
        log.info("[SpringCacheEventListener] onMessage cacheType: {}", cacheType);

        BaseCacheService cacheHandler = handlerMap.get(cacheType);
        if (cacheHandler != null) {
            cacheHandler.evict();
        }
    }
}
