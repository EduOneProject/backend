package io.github.eduoneproject.eduone.business.common.enums.verification;

import lombok.Getter;

/**
 * 验证类型
 *
 * @author summerain0
 */
public enum VerificationType {
    /**
     * 邮箱
     */
    EMAIL("邮箱验证"),

    /**
     * 图形验证码
     */
    IMAGE_CAPTCHA("图形验证码");

    /**
     * 描述
     */
    @Getter
    private final String description;

    VerificationType(String description) {
        this.description = description;
    }
}
