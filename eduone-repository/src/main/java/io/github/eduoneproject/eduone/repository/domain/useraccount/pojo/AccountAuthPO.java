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
    @TableId("id")
    private String id;

    /**
     * 账号ID
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 认证类型
     */
    @TableField("auth_type")
    private String authType;

    /**
     * 认证标识
     */
    @TableField("auth_key")
    private String authKey;

    /**
     * 认证凭据
     */
    @TableField("auth_secret")
    private String authSecret;

    /**
     * 连续失败次数
     */
    @TableField("fail_count")
    private Integer failCount;

    /**
     * 最大失败次数
     */
    @TableField("max_fail_count")
    private Integer maxFailCount;

    /**
     * 锁定截止时间
     */
    @TableField("lock_until")
    private OffsetDateTime lockUntil;

    /**
     * 最后认证时间
     */
    @TableField("last_auth_time")
    private OffsetDateTime lastAuthTime;

    /**
     * 最后认证IP
     */
    @TableField("last_auth_ip")
    private String lastAuthIp;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 凭据过期时间
     */
    @TableField("expire_time")
    private OffsetDateTime expireTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private OffsetDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private OffsetDateTime updateTime;
}