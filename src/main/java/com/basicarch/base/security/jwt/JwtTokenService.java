package com.basicarch.base.security.jwt;

import com.basicarch.base.exception.SystemErrorCode;
import com.basicarch.base.exception.ToyAssert;
import com.basicarch.base.security.jwt.token.RefreshTokenStore;
import com.basicarch.base.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenStore refreshTokenStore;
    private final JwtProperties jwtProperties;

    /**
     * 유저 정보를 가지고 RefreshToken, AccessToken 생성 + 저장
     */
    public JwtTokenInfo createJwtTokenInfo(Authentication authentication) {
        String loginId = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String refreshToken = jwtTokenProvider.createRefreshToken(loginId, authorities);
        String accessToken = jwtTokenProvider.createAccessToken(refreshToken);

        refreshTokenStore.save(loginId, refreshToken, jwtProperties.getRefreshToken().getExpirationDays());

        return JwtTokenInfo.builder()
                .grantType(jwtProperties.getGrantType())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * refreshToken으로 accessToken 재발급 + 중복로그인 체크
     */
    public JwtTokenInfo issueAccessToken(JwtTokenInfo jwtTokenInfo) {
        String refreshToken = jwtTokenInfo.getRefreshToken();

        ToyAssert.notBlank(refreshToken, SystemErrorCode.TOKEN_NOT_FOUND);

        if (jwtTokenProvider.validateToken(refreshToken)) {
            String loginId = jwtTokenProvider.parseClaimsByToken(refreshToken).getSubject();
            String storedRefreshToken = refreshTokenStore.findByLoginId(loginId);

            ToyAssert.isTrue(
                    storedRefreshToken != null && storedRefreshToken.equals(refreshToken),
                    SystemErrorCode.DUPLICATE_LOGIN
            );
        }

        return JwtTokenInfo.builder()
                .grantType(jwtProperties.getGrantType())
                .accessToken(jwtTokenProvider.createAccessToken(refreshToken))
                .build();
    }

    /**
     * 로그아웃 시 리프레시 토큰 삭제
     */
    public void removeRefreshToken(String loginId) {
        if (StringUtils.isNotBlank(loginId)) {
            refreshTokenStore.deleteByLoginId(loginId);
        }
    }
}
