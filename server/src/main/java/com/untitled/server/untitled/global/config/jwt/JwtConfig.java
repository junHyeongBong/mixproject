package com.untitled.server.untitled.global.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    @Bean
    public JwtProvider tokenProvider(JwtProperties jwtProperties) {
        return new JwtProvider(
                jwtProperties.getSecret(),
                jwtProperties.getAccessTokenValidityInSeconds(),
                jwtProperties.getRefreshTokenValidityInSeconds()
        );
    }
}
