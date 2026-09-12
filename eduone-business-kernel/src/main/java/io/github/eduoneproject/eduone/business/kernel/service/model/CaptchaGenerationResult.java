package io.github.eduoneproject.eduone.business.kernel.service.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * 验证码生成结果
 *
 * @author summerain0
 */
@Data
public class CaptchaGenerationResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String uniqueId;

    /**
     * 携带数据
     */
    private T data;

    /**
     * 过期时间
     */
    private OffsetDateTime expirationTime;

    /**
     * 过期时间
     */
    private Long ttl;

    /**
     * 扩展信息
     */
    private Map<String, Object> extensions;
}
