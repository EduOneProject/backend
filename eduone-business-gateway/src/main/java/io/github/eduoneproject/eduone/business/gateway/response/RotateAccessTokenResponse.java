package io.github.eduoneproject.eduone.business.gateway.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 轮换令牌响应
 *
 * @author summerain0
 */
@Data
public class RotateAccessTokenResponse implements Serializable {
    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;
}
