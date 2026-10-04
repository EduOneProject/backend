package io.github.eduoneproject.eduone.business.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 过期时间配置
 *
 * @author summerain0
 */
@Data
@Component
@ConfigurationProperties("expiration")
public class ExpirationConfig {
    /**
     * 图形验证码过期时间
     */
    private Duration imageCaptcha;

    /**
     * 访问令牌过期时间
     */
    private Duration accessToken;

    /**
     * 刷新令牌过期时间
     */
    private Duration refreshToken;
}
