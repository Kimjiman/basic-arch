package com.basicarch.base.cache.spring;

import com.basicarch.base.cache.CacheEventPublisher;
import com.basicarch.base.constants.CacheType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

/**
 * packageName    : com.basicarch.base.cache
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
    public void doPublish(CacheType cacheType) {
        applicationEventPublisher.publishEvent(new SpringCacheInvalidateEvent(cacheType));
    }
}
