package io.github.eduoneproject.eduone.business.common.consts;

import lombok.Getter;

/**
 * 业务异常Code
 * <p>code格式：3位http状态码+2位模块编号+3位序号</p>
 * <p>00模块编号代表这个错误码是全局通用的，如参数错误、无法处理的内部错误等</p>
 *
 * @author summerain0
 */
public enum BusinessExceptionCode {
    // 验证上下文
    VERIFICATION_CONTEXT_INVALID(400_001_001, "验证上下文无效"),
    VERIFICATION_CONTEXT_EXPIRED(400_001_002, "验证上下文已过期，请重新验证"),
    VERIFICATION_CONTEXT_NOT_COMPLETE(400_001_003, "验证未完成，无法执行此操作"),
    VERIFICATION_STEP_NOT_REQUIRED(400_001_004, "当前场景无需此类验证"),
    VERIFICATION_STEP_ALREADY_VERIFIED(409_001_005, "当前验证已通过，请勿重复操作"),

    // 验证码
    CAPTCHA_EXPIRED(400_002_001, "验证码已过期，请重新获取"),
    CAPTCHA_INVALID(400_002_002, "验证码错误"),

    // 鉴权部分
    ACCOUNT_NOT_FOUND(500_003_001, "账号不存在"),
    ACCOUNT_PASSWORD_INVALID(500_003_002, "账号密码错误"),
    ACCOUNT_INACTIVE(500_003_003, "账号已禁用"),
    ACCOUNT_AUTH_INACTIVE(500_003_004, "账号认证已禁用");

    /**
     * 错误码
     */
    @Getter
    private final int code;

    /**
     * 错误描述
     */
    @Getter
    private final String description;

    /**
     * 枚举构造函数
     *
     * @param code        错误码
     * @param description 错误描述
     */
    BusinessExceptionCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
