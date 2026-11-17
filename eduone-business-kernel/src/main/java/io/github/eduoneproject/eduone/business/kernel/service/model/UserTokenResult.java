package io.github.eduoneproject.eduone.business.kernel.service.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户token结果
 *
 * @author summerain0
 */
@Data
public class UserTokenResult implements Serializable {
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
