package io.github.eduoneproject.eduone.business.gateway.enums;

import lombok.Getter;

/**
 * 通用响应枚举
 *
 * @author summerain0
 */
public enum CommonResponseCode {
    SUCCESS("200", "成功"),
    FAILURE("500", "失败");

    /**
     * 响应码
     */
    @Getter
    private final String code;

    /**
     * 响应消息
     */
    @Getter
    private final String message;

    /**
     * 枚举构造函数
     *
     * @param code    响应码
     * @param message 响应消息
     */
    CommonResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}