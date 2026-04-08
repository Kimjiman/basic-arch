package com.example.basicarch.base.cache.kafka;

import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.service.BaseCacheService;
import com.example.basicarch.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Kafka 기반 캐시 무효화 이벤트 수신
 * packageName    : com.example.basicarch.base.cache
 * fileName       : RedisCacheEventListener
 * author         : KIM JIMAN
 * date           : 26. 3. 27. 금요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 27.     KIM JIMAN      First Commit
 */
@Slf4j
@Component
public class KafkaCacheEventListener {
    private final Map<CacheType, BaseCacheService> handlerMap;

    public KafkaCacheEventListener(List<BaseCacheService> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(BaseCacheService::getCacheType, it -> it));
    }

    @KafkaListener(topics = KafkaConfig.INVALIDATE_TOPIC, groupId = "basic-arch")
    public void onMessage(String message) {
        log.info("[KafkaCacheEventListener] onMessage: {}", message);
        CacheType cacheType = CacheType.fromValue(message);
        BaseCacheService cacheHandler = handlerMap.get(cacheType);
        if (cacheHandler != null) {
            cacheHandler.evict();
        }
    }

    /**
     * 재시도 카운트 전부 실패 시 > DLT로 이동된 메시지 처리
     * 현재는 로그만 남기고 추후 알림 등 확장 가능
     */
    @DltHandler
    public void onDltMessage(String message) {
        log.error("[KafkaCacheEventListener] DLT message received. message: {}", message);
    }
}
