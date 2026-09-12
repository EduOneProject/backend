package io.github.eduoneproject.eduone.business.gateway.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 获取验证码请求体
 *
 * @author summerain0
 */
@Data
public class ApplyImageCaptchaRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 验证场景
     */
    @NotBlank(message = "验证场景不能为空")
    private String verificationScene;

    /**
     * 验证上下文token
     */
    @NotBlank(message = "验证上下文token不能为空")
    private String token;
}
