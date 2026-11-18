package io.github.eduoneproject.eduone.business.kernel.service.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 创建刷新令牌参数
 *
 * @author summerain0
 */
@Data
public class CreateRefreshTokenParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * IP地址
     */
    private String ipAddress;

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
        if (sessionId == null) {
            throw new IllegalArgumentException("会话ID不能为空");
        }
        if (accountId == null) {
            throw new IllegalArgumentException("账户ID不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (issuedAt == null) {
            throw new IllegalArgumentException("签发时间不能为空");
        }
        if (expirationTime == null) {
            throw new IllegalArgumentException("过期时间不能为空");
        }
        if (expirationTime.isBefore(issuedAt)) {
            throw new IllegalArgumentException("过期时间不能早于签发时间");
        }
    }
}
