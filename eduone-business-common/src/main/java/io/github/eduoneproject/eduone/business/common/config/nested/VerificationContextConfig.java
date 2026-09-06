package io.github.eduoneproject.eduone.business.common.config.nested;

import lombok.Data;

import java.util.Map;

/**
 * 验证上下文配置信息
 *
 * @author summerain0
 */
@Data
public class VerificationContextConfig {
    /**
     * 验证上下文配置详细信息
     */
    private Map<String, String> context;
}
