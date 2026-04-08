package com.basicarch.base.security.jwt.token;

import com.basicarch.base.redis.RedisObject;
import com.basicarch.base.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenStore implements RefreshTokenStore {
    private static final String KEY_PREFIX = "jwt:refresh:";
    private final RedisRepository redisRepository;

    @Override
    public void save(String loginId, String refreshToken, long expirationDays) {
        redisRepository.save(
                RedisObject.builder()
                        .key(KEY_PREFIX + loginId)
                        .value(refreshToken)
                        .expirationDay(expirationDays)
                        .build()
        );
    }

    @Override
    public String findByLoginId(String loginId) {
        RedisObject redisObject = redisRepository.findValueByKey(KEY_PREFIX + loginId);
        return redisObject.getValue();
    }

    @Override
    public void deleteByLoginId(String loginId) {
        redisRepository.deleteRawByKey(KEY_PREFIX + loginId);
    }
}
