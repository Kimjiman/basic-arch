package com.example.basicarch.base.cache;

import com.example.basicarch.base.constants.CacheType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * packageName    : com.example.basicarch.base.cache
 * fileName       : RedisCacheEventPublisher
 * author         : KIM JIMAN
 * date           : 26. 3. 17. 화요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 17.     KIM JIMAN      First Commit
 */
@RequiredArgsConstructor
public class RedisCacheEventPublisher implements CacheEventPublisher {
    private final StringRedisTemplate stringRedisTemplate;

    public void publish(CacheType cacheType) {
        // Claude catch Critical Error
        // @Transactional + @CacheInvalidate가 같이 붙은 메서드에서 AOP(@AfterReturning)는 메서드 반환 직후 실행되는데, 이 시점은 트랜잭션 커밋 전이다.
        // 트랜잭션 활성 중이면 커밋 완료 후 PUBLISH 되도록 afterCommit 콜백 등록해야한다.
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    doPublish(cacheType);
                }
            });
        } else {
            doPublish(cacheType);
        }
    }

    private void doPublish(CacheType cacheType) {
        // DB 변경(저장/수정/삭제) 완료 후, Redis PUBLISH 커맨드로 cache:invalidate 채널에 캐시 타입명을 전송한다.
        // 채널을 구독 중인 모든 서버가 신호를 수신하면 기존 캐시를 삭제(evict)하고 DB에서 다시 조회해 캐싱한다.
        stringRedisTemplate.convertAndSend(CacheType.INVALIDATE_CHANNEL, cacheType.getCacheName());
    }
}
