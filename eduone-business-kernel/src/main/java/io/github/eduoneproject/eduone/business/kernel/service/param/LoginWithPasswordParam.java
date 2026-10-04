package io.github.eduoneproject.eduone.business.kernel.service.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录请求参数
 *
 * @author summerain0
 */
@Data
public class LoginWithPasswordParam implements Serializable {
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
     * 登录令牌
     */
    private String token;
}
