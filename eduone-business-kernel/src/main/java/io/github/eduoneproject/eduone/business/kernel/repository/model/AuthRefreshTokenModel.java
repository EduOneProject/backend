package io.github.eduoneproject.eduone.business.kernel.repository.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 刷新令牌记录信息
 *
 * @author summerain0
 */
@Data
public class AuthRefreshTokenModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * RefreshToken的SHA-256哈希
     */
    private String tokenHash;

    /**
     * 所属会话 ID
     */
    private String sessionId;

    /**
     * 所属账号 ID
     */
    private String accountId;

    /**
     * 所属用户 ID
     */
    private String userId;

    /**
     * 过期时间
     */
    private OffsetDateTime expiresTime;

    /**
     * 是否已被轮换使用
     */
    private Boolean used;

    /**
     * 被轮换使用的时间
     */
    private OffsetDateTime usedTime;

    /**
     * 创建时间
     */
    private OffsetDateTime createdTime;

    /**
     * 是否已撤销
     */
    private Boolean revoked;

    /**
     * 撤销时间
     */
    private OffsetDateTime revokedTime;

    /**
     * 轮换后的新Token ID，用于追踪Token家族
     */
    private String replacedBy;

    /**
     * 设备标识
     */
    private String deviceId;

    /**
     * 客户端 UA
     */
    private String userAgent;

    /**
     * 客户端 IP
     */
    private String ipAddress;
}
