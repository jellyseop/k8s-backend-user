package com.welab.k8s_backend_user.secret.jwt.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "jwt")
@Getter @Setter
public class JwtConfigProperites {
    private Integer expiresIn;
    private Integer mobileExpiresIn;
    private Integer tableExpiresIn;
    private String secretKey;
}
