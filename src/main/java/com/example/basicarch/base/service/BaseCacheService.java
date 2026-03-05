package com.example.basicarch.base.service;

import com.example.basicarch.base.constants.CacheType;

public interface BaseCacheService {
    CacheType getCacheType();
    void evict();
}
