package io.github.eduoneproject.eduone.common.exception;

import io.github.eduoneproject.eduone.common.consts.CommonExceptionCode;
import lombok.Getter;

import java.io.Serial;

/**
 * 基础运行时异常
 *
 * @author summerain0
 */
public class BasicRuntimeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 异常码
     */
    @Getter
    private final int code;

    /**
     * 构造方法
     *
     * @param code    异常码
     * @param message 异常信息
     * @param cause   异常原因
     */
    public BasicRuntimeException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 构造方法
     *
     * @param message 异常信息
     * @param cause   异常原因
     */
    public BasicRuntimeException(String message, Throwable cause) {
        this(CommonExceptionCode.INTERNAL_ERROR.getCode(), message, cause);
    }

    /**
     * 构造方法
     *
     * @param code    异常码
     * @param message 异常信息
     */
    public BasicRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造方法
     *
     * @param message 异常信息
     */
    public BasicRuntimeException(String message) {
        this(CommonExceptionCode.INTERNAL_ERROR.getCode(), message);
    }
}
