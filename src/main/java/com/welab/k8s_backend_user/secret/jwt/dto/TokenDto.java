package com.welab.k8s_backend_user.secret.jwt.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class JwtToken{
        private String token;
        private Integer expiresIn;
    }

    @RequiredArgsConstructor
    @Getter
    public static class AccessToken {
        private final JwtToken access;
    }

    @RequiredArgsConstructor
    @Getter
    public static class AccessRefreshToken {
        private final JwtToken access;
        private final JwtToken refresh;
    }
}
