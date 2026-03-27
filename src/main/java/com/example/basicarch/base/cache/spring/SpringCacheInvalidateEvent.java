package com.example.basicarch.base.cache.spring;

import com.example.basicarch.base.constants.CacheType;

/**
 * packageName    : com.example.basicarch.base.cache
 * fileName       : CacheInvalidateEvent
 * author         : KIM JIMAN
 * date           : 26. 3. 27. 금요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 3. 27.     KIM JIMAN      First Commit
 */
public record SpringCacheInvalidateEvent(CacheType cacheType) {
}
