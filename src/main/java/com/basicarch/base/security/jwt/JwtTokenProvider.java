package com.basicarch.base.security.jwt;

import com.basicarch.base.exception.CustomException;
import com.basicarch.base.exception.SystemErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final Key key;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * loginId와 권한으로 refreshToken 생성
     */
    public String createRefreshToken(String loginId, String authorities) {
        Date expiration = new Date(System.currentTimeMillis() + jwtProperties.getRefreshToken().getExpiration());

        return Jwts.builder()
                .setSubject(loginId)
                .claim(jwtProperties.getClaimName(), authorities)
                .setExpiration(expiration)
                .setIssuer(loginId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * refreshToken을 이용해서 accessToken 생성
     */
    public String createAccessToken(String refreshToken) {
        Date expiration = new Date(System.currentTimeMillis() + jwtProperties.getAccessToken().getExpiration());

        Claims claims = this.parseClaimsByToken(refreshToken);
        String loginId = claims.getSubject();
        String auth = (String) claims.get(jwtProperties.getClaimName());

        return Jwts.builder()
                .setSubject(loginId)
                .claim(jwtProperties.getClaimName(), auth)
                .setExpiration(expiration)
                .setIssuer(loginId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * token으로 claims 추출
     */
    public Claims parseClaimsByToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
     */
    public Authentication getAuthenticationByToken(String token) {
        Claims claims = parseClaimsByToken(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(jwtProperties.getClaimName()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new CustomException(SystemErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(SystemErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(SystemErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(SystemErrorCode.EMPTY_CLAIMS);
        } catch (JwtException e) {
            throw new CustomException(SystemErrorCode.TOKEN_PROCESSING_ERROR);
        }
    }
}
