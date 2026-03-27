package com.example.basicarch.base.cache.spring;

import com.example.basicarch.base.cache.CacheEventPublisher;
import com.example.basicarch.base.constants.CacheType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * packageName    : com.example.basicarch.base.cache
 * fileName       : SpringCacheEventPublisher
 * author         : KIM JIMAN
 * date           : 26. 3. 27. 금요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 27.     KIM JIMAN      First Commit
 */
@RequiredArgsConstructor
public class SpringCacheEventPublisher implements CacheEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(CacheType cacheType) {
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
        applicationEventPublisher.publishEvent(new SpringCacheInvalidateEvent(cacheType));
    }
}
