package com.basicarch.base.cache;

import com.basicarch.base.constants.CacheType;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * packageName    : com.basicarch.base.cache
 * fileName       : CacheEventPublisher
 * author         : KIM JIMAN
 * date           : 26. 3. 5. 화요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 5.     KIM JIMAN      First Commit
 */
public interface CacheEventPublisher {
    void doPublish(CacheType cacheType);

    default void publish(CacheType cacheType) {
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

}
