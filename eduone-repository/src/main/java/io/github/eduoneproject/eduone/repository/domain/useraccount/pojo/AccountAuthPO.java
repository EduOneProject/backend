package io.github.eduoneproject.eduone.repository.domain.useraccount.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 账号认证信息表
 *
 * @author summerain0
 */
@Data
@Accessors(chain = true)
@TableName("bds_account_auth")
public class AccountAuthPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    public static final String COLUMN_ID = "id";

    /**
     * 账号ID
     */
    public static final String COLUMN_ACCOUNT_ID = "account_id";

    /**
     * 认证类型
     */
    public static final String COLUMN_AUTH_TYPE = "auth_type";

    /**
     * 认证标识
     */
    public static final String COLUMN_AUTH_KEY = "auth_key";

    /**
     * 认证凭据
     */
    public static final String COLUMN_AUTH_SECRET = "auth_secret";

    /**
     * 连续失败次数
     */
    public static final String COLUMN_FAIL_COUNT = "fail_count";

    /**
     * 最大失败次数
     */
    public static final String COLUMN_MAX_FAIL_COUNT = "max_fail_count";

    /**
     * 锁定截止时间
     */
    public static final String COLUMN_LOCK_UNTIL = "lock_until";

    /**
     * 最后认证时间
     */
    public static final String COLUMN_LAST_AUTH_TIME = "last_auth_time";

    /**
     * 最后认证IP
     */
    public static final String COLUMN_LAST_AUTH_IP = "last_auth_ip";

    /**
     * 状态
     */
    public static final String COLUMN_STATUS = "status";

    /**
     * 凭据过期时间
     */
    public static final String COLUMN_EXPIRE_TIME = "expire_time";

    /**
     * 创建时间
     */
    public static final String COLUMN_CREATE_TIME = "create_time";

    /**
     * 更新时间
     */
    public static final String COLUMN_UPDATE_TIME = "update_time";

    /**
     * 主键
     */
    @TableId(COLUMN_ID)
    private String id;

    /**
     * 账号ID
     */
    @TableField(COLUMN_ACCOUNT_ID)
    private String accountId;

    /**
     * 认证类型
     */
    @TableField(COLUMN_AUTH_TYPE)
    private String authType;

    /**
     * 认证标识
     */
    @TableField(COLUMN_AUTH_KEY)
    private String authKey;

    /**
     * 认证凭据
     */
    @TableField(COLUMN_AUTH_SECRET)
    private String authSecret;

    /**
     * 连续失败次数
     */
    @TableField(COLUMN_FAIL_COUNT)
    private Integer failCount;

    /**
     * 最大失败次数
     */
    @TableField(COLUMN_MAX_FAIL_COUNT)
    private Integer maxFailCount;

    /**
     * 锁定截止时间
     */
    @TableField(COLUMN_LOCK_UNTIL)
    private OffsetDateTime lockUntil;

    /**
     * 最后认证时间
     */
    @TableField(COLUMN_LAST_AUTH_TIME)
    private OffsetDateTime lastAuthTime;

    /**
     * 最后认证IP
     */
    @TableField(COLUMN_LAST_AUTH_IP)
    private String lastAuthIp;

    /**
     * 状态
     */
    @TableField(COLUMN_STATUS)
    private String status;

    /**
     * 凭据过期时间
     */
    @TableField(COLUMN_EXPIRE_TIME)
    private OffsetDateTime expireTime;

    /**
     * 创建时间
     */
    @TableField(COLUMN_CREATE_TIME)
    private OffsetDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(COLUMN_UPDATE_TIME)
    private OffsetDateTime updateTime;
}