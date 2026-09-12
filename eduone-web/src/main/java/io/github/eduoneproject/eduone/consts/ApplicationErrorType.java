package io.github.eduoneproject.eduone.consts;

import graphql.ErrorClassification;
import lombok.Getter;

/**
 * 应用GraphQL错误分类
 *
 * @author summerain0
 */
public enum ApplicationErrorType implements ErrorClassification {
    BAD_REQUEST(400, "参数/请求错误，客户端可修正后重试"),
    UNAUTHORIZED(401, "未认证，需登录或刷新凭证"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    CONFLICT(409, "状态冲突（重复提交、状态已变更）"),
    TOO_MANY_REQUESTS(429, "限流，可延迟重试"),
    INTERNAL_ERROR(500, "服务端内部错误，可重试");

    /**
     * HTTP状态码
     */
    @Getter
    private final int httpStatus;

    /**
     * 描述
     */
    @Getter
    private final String description;

    /**
     * 枚举构造函数
     *
     * @param httpStatus  HTTP状态码
     * @param description 描述
     */
    ApplicationErrorType(int httpStatus, String description) {
        this.httpStatus = httpStatus;
        this.description = description;
    }

    /**
     * 根据状态码获取枚举值
     *
     * @param code 状态码
     * @return 枚举值，默认值为INTERNAL_ERROR
     */
    public static ApplicationErrorType valueOf(int code) {
        for (ApplicationErrorType type : ApplicationErrorType.values()) {
            if (type.httpStatus == code) {
                return type;
            }
        }
        return ApplicationErrorType.INTERNAL_ERROR;
    }

    /**
     * 是否可重试
     *
     * @param type 错误类型
     * @return 是否可重试
     */
    public static boolean retryable(ApplicationErrorType type) {
        if (type == null) {
            return false;
        }
        return type == ApplicationErrorType.TOO_MANY_REQUESTS
                || type == ApplicationErrorType.INTERNAL_ERROR;
    }
}