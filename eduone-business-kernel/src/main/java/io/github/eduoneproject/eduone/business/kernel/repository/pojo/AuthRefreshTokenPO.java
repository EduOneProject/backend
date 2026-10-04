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
 * RefreshToken 表
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
    public static final String COLUMN_ID = "id";

    /**
     * RefreshToken的SHA-256哈希
     */
    public static final String COLUMN_TOKEN_HASH = "token_hash";

    /**
     * 所属用户 ID
     */
    public static final String COLUMN_USER_ID = "user_id";

    /**
     * 过期时间
     */
    public static final String COLUMN_EXPIRES_TIME = "expires_time";

    /**
     * 是否已被轮换使用
     */
    public static final String COLUMN_USED = "used";

    /**
     * 被轮换使用的时间
     */
    public static final String COLUMN_USED_TIME = "used_time";

    /**
     * 创建时间
     */
    public static final String COLUMN_CREATED_TIME = "created_time";

    /**
     * 是否已撤销
     */
    public static final String COLUMN_REVOKED = "revoked";

    /**
     * 撤销时间
     */
    public static final String COLUMN_REVOKED_TIME = "revoked_time";

    /**
     * 轮换后的新Token ID，用于追踪Token家族
     */
    public static final String COLUMN_REPLACED_BY = "replaced_by";

    /**
     * 设备标识
     */
    public static final String COLUMN_DEVICE_ID = "device_id";

    /**
     * 客户端 UA
     */
    public static final String COLUMN_USER_AGENT = "user_agent";

    /**
     * 客户端 IP
     */
    public static final String COLUMN_IP_ADDRESS = "ip_address";

    /**
     * 主键
     */
    @TableId(COLUMN_ID)
    private String id;

    /**
     * RefreshToken的SHA-256哈希
     */
    @TableField(COLUMN_TOKEN_HASH)
    private String tokenHash;

    /**
     * 所属用户 ID
     */
    @TableField(COLUMN_USER_ID)
    private String userId;

    /**
     * 过期时间
     */
    @TableField(COLUMN_EXPIRES_TIME)
    private OffsetDateTime expiresTime;

    /**
     * 是否已被轮换使用
     */
    @TableField(COLUMN_USED)
    private Boolean used;

    /**
     * 被轮换使用的时间
     */
    @TableField(COLUMN_USED_TIME)
    private OffsetDateTime usedTime;

    /**
     * 创建时间
     */
    @TableField(COLUMN_CREATED_TIME)
    private OffsetDateTime createdTime;

    /**
     * 是否已撤销
     */
    @TableField(COLUMN_REVOKED)
    private Boolean revoked;

    /**
     * 撤销时间
     */
    @TableField(COLUMN_REVOKED_TIME)
    private OffsetDateTime revokedTime;

    /**
     * 轮换后的新Token ID，用于追踪Token家族
     */
    @TableField(COLUMN_REPLACED_BY)
    private String replacedBy;

    /**
     * 设备标识
     */
    @TableField(COLUMN_DEVICE_ID)
    private String deviceId;

    /**
     * 客户端 UA
     */
    @TableField(COLUMN_USER_AGENT)
    private String userAgent;

    /**
     * 客户端 IP
     */
    @TableField(COLUMN_IP_ADDRESS)
    private String ipAddress;
}