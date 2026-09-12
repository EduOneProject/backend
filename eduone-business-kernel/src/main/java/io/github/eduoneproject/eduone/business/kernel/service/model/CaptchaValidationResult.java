package io.github.eduoneproject.eduone.business.kernel.service.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 验证码校验结果
 *
 * @author summerain0
 */
@Data
public class CaptchaValidationResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String uniqueId;

    /**
     * 是否校验通过
     */
    private boolean isValid;
}
