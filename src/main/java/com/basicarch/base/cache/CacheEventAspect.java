package com.basicarch.base.cache;

import com.basicarch.base.annotation.CacheInvalidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @CacheInvalidate 실제 구현체
 * packageName    : com.basicarch.base.cache
 * fileName       : CacheEventAspect
 * author         : KIM JIMAN
 * date           : 26. 3. 5. 화요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 5.     KIM JIMAN      First Commit
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheEventAspect {
    private final CacheEventPublisher cacheEventPublisher;

    @AfterReturning("@annotation(cacheInvalidate)")
    public void invalidate(CacheInvalidate cacheInvalidate) {
        cacheEventPublisher.publish(cacheInvalidate.value());
    }
}
