package io.github.eduoneproject.eduone.business.kernel.repository.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 刷新令牌表
 *
 * @author summerain0
 */
@Data
@Accessors(chain = true)
@TableName("bds_auth_refresh_tokens")
public class AuthRefreshTokenPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * RefreshToken的SHA-256哈希
     */
    @TableField("token_hash")
    private String tokenHash;

    /**
     * 所属会话 ID
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * 所属账号 ID
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 所属用户 ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 过期时间
     */
    @TableField("expires_time")
    private OffsetDateTime expiresTime;

    /**
     * 是否已被轮换使用
     */
    @TableField("used")
    private Boolean used;

    /**
     * 被轮换使用的时间
     */
    @TableField("used_time")
    private OffsetDateTime usedTime;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private OffsetDateTime createdTime;

    /**
     * 是否已撤销
     */
    @TableField("revoked")
    private Boolean revoked;

    /**
     * 撤销时间
     */
    @TableField("revoked_time")
    private OffsetDateTime revokedTime;

    /**
     * 轮换后的新Token ID，用于追踪Token家族
     */
    @TableField("replaced_by")
    private String replacedBy;

    /**
     * 设备标识
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 客户端 UA
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 客户端 IP
     */
    @TableField("ip_address")
    private String ipAddress;
}
