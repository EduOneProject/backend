package io.github.eduoneproject.eduone.business.common.domain.verification;

import io.github.eduoneproject.eduone.business.common.enums.verification.VerificationType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 验证步骤
 *
 * @author summerain0
 */
@Data
public class VerificationStep implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 验证类型
     */
    private VerificationType verificationType;

    /**
     * 验证场景
     */
    private String verificationScene;

    /**
     * 唯一标识
     */
    private String uniqueId;

    /**
     * 验证对象
     */
    private String verifyObject;

    /**
     * 是否完成
     */
    private boolean completed;

    /**
     * 过期时间(毫秒)
     */
    private Long expireTime;
}
