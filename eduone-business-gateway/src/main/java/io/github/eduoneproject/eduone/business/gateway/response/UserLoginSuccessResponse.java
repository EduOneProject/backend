package io.github.eduoneproject.eduone.business.gateway.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录成功响应体
 *
 * @author summerain0
 */
@Data
public class UserLoginSuccessResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;
}
