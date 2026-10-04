package io.github.eduoneproject.eduone.business.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 加解密密钥配置
 *
 * @author summerain0
 */
@Data
@Component
@ConfigurationProperties("crypto-key")
public class CryptoKeyConfig {
    /**
     * 验证上下文
     */
    private String verificationContext;

    /**
     * 访问令牌JWT密钥
     */
    private String accessTokenJwtSecret;
}
