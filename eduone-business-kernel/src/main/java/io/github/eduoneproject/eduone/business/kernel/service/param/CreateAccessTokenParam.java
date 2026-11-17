package io.github.eduoneproject.eduone.business.kernel.service.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 创建访问令牌参数
 *
 * @author summerain0
 */
@Data
public class CreateAccessTokenParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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

    /**
     * 签发时间
     */
    private OffsetDateTime issuedAt;

    /**
     * 过期时间
     */
    private OffsetDateTime expirationTime;
}
