package io.github.eduoneproject.eduone.business.gateway.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 验证图形验证码请求体
 *
 * @author summerain0
 */
@Data
public class ImageCaptchaValidRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 验证场景
     */
    @NotBlank(message = "验证场景不能为空")
    private String verificationScene;

    /**
     * 校验上下文token
     */
    @NotBlank(message = "校验上下文token不能为空")
    private String token;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
}
