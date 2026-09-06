package io.github.eduoneproject.eduone.business.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 前端应用配置信息
 *
 * @author summerain0
 */
@Data
@Component
@ConfigurationProperties("frontend-application")
public class FrontendApplicationConfig {
    /**
     * 验证上下文配置信息
     */
    private Map<String, String> verificationContext;
}
