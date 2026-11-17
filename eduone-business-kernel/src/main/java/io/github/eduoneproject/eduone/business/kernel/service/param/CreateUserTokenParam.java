package io.github.eduoneproject.eduone.business.kernel.service.param;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 创建用户令牌参数
 *
 * @author summerain0
 */
@Data
public class CreateUserTokenParam implements Serializable {
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

    /**
     * 验证参数
     */
    public void validate() {
        if (StringUtils.isAnyBlank(sessionId, userId, accountId)) {
            throw new IllegalArgumentException("会话ID、用户ID和账户ID不能为空");
        }
        if (issuedAt == null || expirationTime == null) {
            throw new IllegalArgumentException("签发时间和过期时间不能为空");
        }
        if (expirationTime.isBefore(issuedAt)) {
            throw new IllegalArgumentException("过期时间不能早于签发时间");
        }
    }
}
