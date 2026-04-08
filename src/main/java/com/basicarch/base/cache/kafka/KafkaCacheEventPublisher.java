package com.basicarch.base.cache.kafka;

import com.basicarch.base.cache.CacheEventPublisher;
import com.basicarch.base.constants.CacheType;
import com.basicarch.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Kafka 기반 캐시 무효화 이벤트 발행
 * packageName    : com.basicarch.base.cache
 * fileName       : RedisCacheEventListener
 * author         : KIM JIMAN
 * date           : 26. 3. 27. 금요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 27.     KIM JIMAN      First Commit
 */
@RequiredArgsConstructor
public class KafkaCacheEventPublisher implements CacheEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void doPublish(CacheType cacheType) {
        kafkaTemplate.send(KafkaConfig.INVALIDATE_TOPIC, cacheType.getCacheName());
    }
}
