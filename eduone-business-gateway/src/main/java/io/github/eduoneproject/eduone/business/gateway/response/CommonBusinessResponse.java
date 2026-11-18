package io.github.eduoneproject.eduone.business.gateway.response;

import io.github.eduoneproject.eduone.business.common.consts.BusinessExceptionCode;
import io.github.eduoneproject.eduone.business.gateway.enums.CommonResponseCode;
import io.github.eduoneproject.eduone.common.consts.CommonExceptionCode;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用业务响应体
 *
 * @author summerain0
 */
@Data
public class CommonBusinessResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 创建成功响应
     *
     * @return 响应体
     */
    public static <T> CommonBusinessResponse<T> success() {
        CommonBusinessResponse<T> response = new CommonBusinessResponse<>();
        response.setCode(CommonResponseCode.SUCCESS.getCode());
        response.setMessage(CommonResponseCode.SUCCESS.getMessage());
        response.setData(null);
        return response;
    }

    /**
     * 创建成功响应
     *
     * @param data 响应数据
     * @return 响应体
     */
    public static <T> CommonBusinessResponse<T> success(T data) {
        CommonBusinessResponse<T> response = new CommonBusinessResponse<>();
        response.setCode(CommonResponseCode.SUCCESS.getCode());
        response.setMessage(CommonResponseCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    /**
     * 创建失败响应
     *
     * @param businessExceptionCode 响应码
     * @return 响应体
     */
    public static <T> CommonBusinessResponse<T> failure(BusinessExceptionCode businessExceptionCode) {
        CommonBusinessResponse<T> response = new CommonBusinessResponse<>();
        response.setCode(String.valueOf(businessExceptionCode.getCode()));
        response.setMessage(businessExceptionCode.getDescription());
        return response;
    }

    /**
     * 创建失败响应
     *
     * @param commonExceptionCode 响应码
     * @param message             响应消息
     * @return 响应体
     */
    public static <T> CommonBusinessResponse<T> failure(CommonExceptionCode commonExceptionCode, String message) {
        CommonBusinessResponse<T> response = new CommonBusinessResponse<>();
        response.setCode(String.valueOf(commonExceptionCode.getCode()));
        response.setMessage(message);
        return response;
    }

    /**
     * 创建失败响应
     *
     * @param e 异常
     * @return 响应体
     */
    public static <T> CommonBusinessResponse<T> failure(BasicRuntimeException e) {
        CommonBusinessResponse<T> response = new CommonBusinessResponse<>();
        response.setCode(String.valueOf(e.getCode()));
        response.setMessage(e.getMessage());
        return response;
    }
}