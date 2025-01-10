package com.untitled.server.untitled.global.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String header;
    private String secret;
    private Long accessTokenValidityInSeconds;
    private Long refreshTokenValidityInSeconds;
}
