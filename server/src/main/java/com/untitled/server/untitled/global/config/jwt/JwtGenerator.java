package com.untitled.server.untitled.global.config.jwt;

import com.untitled.server.untitled.domain.entity.Member;
import com.untitled.server.untitled.global.common.model.JwtModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

public class JwtGenerator {
    public static final String TOKEN_LOGIN_ID_KEY = "loginId";
    public static final String TOKEN_MEMBER_ID_KEY = "memberId";
    public static final String TOKEN_AUTH_KEY = "auth";
    public static final String TOKEN_ID_KEY = "tokenId";
    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtGenerator(String secret, Long accessTokenValidityInSeconds, Long refreshTokenValidityInSeconds) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenValidityInSeconds;
        this.refreshTokenExpTime = refreshTokenValidityInSeconds;
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    protected JwtModel generateToken(Member member) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenExpTime);
        ZonedDateTime refreshTokenValidity = now.plusSeconds(refreshTokenExpTime);

        Claims claims = Jwts.claims();
        claims.put(TOKEN_LOGIN_ID_KEY, member.getLoginId());
        claims.put(TOKEN_MEMBER_ID_KEY, member.getId());
        claims.put(TOKEN_AUTH_KEY, member.getRole());
        claims.put(TOKEN_ID_KEY, UUID.randomUUID().toString());

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(member.getLoginId())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(Date.from(refreshTokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtModel.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }
}