package com.example.basicarch.base.cache;

import com.example.basicarch.base.cache.redis.RedisCacheEventListener;
import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.service.BaseCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CacheEventListenerTest {

    @Mock
    private BaseCacheService codeCacheService;
    @Mock
    private BaseCacheService menuCacheService;

    @Mock
    private Message message;

    private RedisCacheEventListener cacheEventListener;

    @BeforeEach
    void setUp() {
        given(codeCacheService.getCacheType()).willReturn(CacheType.CODE);
        given(menuCacheService.getCacheType()).willReturn(CacheType.MENU);
        cacheEventListener = new RedisCacheEventListener(List.of(codeCacheService, menuCacheService));
    }

    @Test
    void onMessageCodeEvict() {
        given(message.getBody()).willReturn(CacheType.CODE.getCacheName().getBytes());
        cacheEventListener.onMessage(message, null);
        verify(codeCacheService).evict();
    }

    @Test
    void onMessageMenuEvict() {
        given(message.getBody()).willReturn(CacheType.MENU.getCacheName().getBytes());
        cacheEventListener.onMessage(message, null);
        verify(menuCacheService).evict();
        verify(codeCacheService, never()).evict();
    }

    @Test
    void onMessageUnknownTypeNoEvict() {
        given(message.getBody()).willReturn("unknown".getBytes());
        cacheEventListener.onMessage(message, null);
        verify(codeCacheService, never()).evict();
        verify(menuCacheService, never()).evict();
    }
}
