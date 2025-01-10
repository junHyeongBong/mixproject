package com.untitled.server.untitled.global.config.jwt;

import com.untitled.server.untitled.domain.api.auth.exception.MemberAuthException;
import com.untitled.server.untitled.domain.entity.Member;
import com.untitled.server.untitled.global.common.model.JwtModel;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtValidators;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static com.untitled.server.untitled.global.config.jwt.JwtGenerator.TOKEN_ID_KEY;

@Slf4j
public class JwtProvider {
    private final Key key;
    private final String secret;
    private final Long accessTokenValidityInSeconds;
    private final Long refreshTokenValidityInSeconds;

    public JwtProvider(String secret, Long accessTokenValidityInSeconds, Long refreshTokenValidityInSeconds) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.secret = secret;
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
    }

    public JwtModel createToken(Member member) {
        JwtGenerator jwtGenerator = new JwtGenerator(secret,accessTokenValidityInSeconds,refreshTokenValidityInSeconds);
        return jwtGenerator.generateToken(member);
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken, Claims claims) {
        // Jwt 토큰 복호화
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(JwtGenerator.TOKEN_AUTH_KEY).toString()
                .split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // Refresh Token 유효성 검사 후 Access Token 재발급
    public Optional<String> refreshAccessTokenIfNeeded(String token, Member member) {
        Claims refreshTokenClaims = validateRefreshToken(token);
        if (refreshTokenClaims == null) {
            return Optional.empty();    // Refresh Token이 유효하지 않으면 Optional.empty() 반환
        }
        JwtModel jwtModel = createToken(member);
        return Optional.of(jwtModel.getAccessToken());
    }

    private Claims validateRefreshToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;    //refresh token이 유효하지않은경우 null 반환
        }
    }

    // 토큰 정보를 검증하는 메서드
    public JwtValidation validateToken(String token) throws MemberAuthException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return new JwtValidation(
                    JwtValidation.TokenStatus.TOKEN_VALID,
                    JwtValidation.TokenType.ACCESS,
                    claims.get(TOKEN_ID_KEY, String.class),
                    claims
            );
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT token", e);
            return new JwtValidation(JwtValidation.TokenStatus.TOKEN_WRONG_SIGNATURE, null , null, null);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token", e);
            return new JwtValidation(JwtValidation.TokenStatus.TOKEN_EXPIRED, null , null, null);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token", e);
            return new JwtValidation(JwtValidation.TokenStatus.TOKEN_HASH_NOT_SUPPORTED, null , null, null);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
            return new JwtValidation(JwtValidation.TokenStatus.TOKEN_WRONG_SIGNATURE, null, null , null);
        }
    }

    // accessToken
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }

    }

    


}
