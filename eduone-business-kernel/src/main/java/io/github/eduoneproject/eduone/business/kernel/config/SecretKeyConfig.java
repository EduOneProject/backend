package io.github.eduoneproject.eduone.business.kernel.config;

import io.github.eduoneproject.eduone.business.common.config.CryptoKeyConfig;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * 加解密Key配置
 *
 * @author summerain0
 */
@Configuration
@RequiredArgsConstructor
public class SecretKeyConfig {
    private final CryptoKeyConfig cryptKeyConfig;

    /**
     * AccessToken加密Key
     */
    @Bean("accessTokenSecretKey")
    public SecretKey accessTokenSecretKey() {
        String accessTokenJwtSecret = cryptKeyConfig.getAccessTokenJwtSecret();
        return Keys.hmacShaKeyFor(accessTokenJwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
