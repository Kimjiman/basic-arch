package com.example.basicarch.base.cache;

import com.example.basicarch.base.constants.CacheType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CacheEventPublisherTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private CacheEventPublisher cacheEventPublisher;

    @Test
    void publishCode() {
        cacheEventPublisher.publish(CacheType.CODE);

        verify(stringRedisTemplate).convertAndSend(CacheType.INVALIDATE_CHANNEL, CacheType.CODE.getCacheName());
    }
}
