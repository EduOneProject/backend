package io.github.eduoneproject.eduone.business.kernel.service.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 验证码生成请求条件
 *
 * @author summerain0
 */
@Data
public class CaptchaGenerationParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
