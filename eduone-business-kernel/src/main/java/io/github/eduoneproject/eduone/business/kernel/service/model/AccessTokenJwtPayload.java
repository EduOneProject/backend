package io.github.eduoneproject.eduone.business.kernel.service.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 访问令牌jwt载体
 *
 * @author summerain0
 */
@Data
public class AccessTokenJwtPayload implements Serializable {
    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 账户ID
     */
    private String accountId;
}
