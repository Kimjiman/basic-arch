package com.basicarch.base.service;

import com.basicarch.base.constants.CacheType;

public interface BaseCacheService {
    CacheType getCacheType();
    void evict();
}
