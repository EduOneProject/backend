package io.github.eduoneproject.eduone.business.kernel.service.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 验证码校验请求参数
 *
 * @author summerain0
 */
@Data
public class CaptchaValidationParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private String uniqueId;

    /**
     * 用户输入值
     */
    private String userInput;
}
