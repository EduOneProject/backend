package io.github.eduoneproject.eduone.business.gateway.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 以密码登录请求体
 *
 * @author summerain0
 */
@Data
public class LoginWithPasswordRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证上下文token
     */
    private String token;
}
