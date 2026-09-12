package io.github.eduoneproject.eduone.business.gateway.response;

import io.github.eduoneproject.eduone.business.gateway.enums.CommonResponseCode;
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
}