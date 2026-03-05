package com.example.basicarch.base.cache;

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
 * fileName       : CacheEventListener
 * author         : KIM JIMAN
 * date           : 26. 3. 5. 화요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 5.     KIM JIMAN      First Commit
 * CacheType.INVALIDATE_CHANNEL 채널을 구독하고 있다가 메시지가 오면 해당 캐시를 삭제한다.
 * RedisConfig에서 이 리스너를 CacheType.INVALIDATE_CHANNEL 채널에 등록한다.
 * 앱 시작 시 RedisConfig에 @Bean으로 등록된 CacheEventListener가 실행되어, BaseCacheService 구현체들을 Map에 담아둔다.
 * 메시지로 받은 CacheType 문자열로 Map을 조회해 BaseCacheService의 실제구현체의 evict()를 호출하여 삭제한다.
 */
@Slf4j
@Component
public class CacheEventListener implements MessageListener {
    private final Map<CacheType, BaseCacheService> handlerMap;

    public CacheEventListener(List<BaseCacheService> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(baseCacheService -> baseCacheService.getCacheType(), it -> it));
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("[CacheEventListener] onMessage start message: {}", message);

        String body = new String(message.getBody());
        log.info("[CacheEventListener] onMessage start body: {}", body);

        try {
            CacheType cacheType = CacheType.fromValue(body);
            log.info("[CacheEventListener] onMessage cacheType: {}", cacheType);
            BaseCacheService cacheHandler = handlerMap.get(cacheType);
            if (cacheHandler != null) {
                cacheHandler.evict();
            }
        } catch (IllegalArgumentException e) {
            log.error("[CacheEventListener] Unknown cache type: {}", body);
        }
    }
}
