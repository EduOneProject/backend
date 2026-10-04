package io.github.eduoneproject.eduone.common.consts;

import lombok.Getter;

/**
 * 通用异常Code
 * <p>code格式：3位http状态码+2位模块编号+3位序号</p>
 * <p>00模块编号代表这个错误码是全局通用的，如参数错误、无法处理的内部错误等</p>
 *
 * @author summerain0
 */
public enum CommonExceptionCode {
    // 全局通用错误码
    PARAM_REQUIRED(400_000_001, "参数不能为空"),
    PARAM_INVALID(400_000_002, "参数非法"),

    // 未登录
    UNAUTHORIZED(401_000_001, "未登录"),

    // 内部异常
    INTERNAL_ERROR(500_000_000, "内部错误");

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
    CommonExceptionCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
