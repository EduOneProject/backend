package io.github.eduoneproject.eduone.business.gateway.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图形验证码响应内容
 *
 * @author summerain0
 */
@Data
public class ImageCaptchaResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 经过base64编码后的验证码图片
     */
    private String imageBase64;

    /**
     * 业务Token
     */
    private String token;
}