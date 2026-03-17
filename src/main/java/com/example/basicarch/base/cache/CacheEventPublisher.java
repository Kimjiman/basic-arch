package com.example.basicarch.base.cache;

import com.example.basicarch.base.constants.CacheType;

/**
 * packageName    : com.example.basicarch.base.cache
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
    void publish(CacheType cacheType);
}
