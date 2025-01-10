package com.untitled.server.untitled.global.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtModel {
    String accessToken;
    String refreshToken;
}
