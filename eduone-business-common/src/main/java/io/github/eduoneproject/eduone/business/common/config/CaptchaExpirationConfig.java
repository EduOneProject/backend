package io.github.eduoneproject.eduone.business.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 验证码过期时间配置
 *
 * @author summerain0
 */
@Data
@Component
@ConfigurationProperties("captcha.expiration")
public class CaptchaExpirationConfig {
    /**
     * 图形验证码过期时间
     */
    private Duration imageCaptcha;
}
