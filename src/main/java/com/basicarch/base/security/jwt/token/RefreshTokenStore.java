package com.basicarch.base.security.jwt.token;

public interface RefreshTokenStore {
    void save(String loginId, String refreshToken, long expirationDays);

    String findByLoginId(String loginId);

    void deleteByLoginId(String loginId);
}
