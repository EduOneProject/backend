package io.github.eduoneproject.eduone.query.contract.view;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 账号认证信息
 *
 * @author summerain0
 */
@Data
public class AccountAuthView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账号ID
     */
    private String accountId;

    /**
     * 认证类型
     */
    private String authType;

    /**
     * 认证标识
     */
    private String authKey;

    /**
     * 认证凭据
     */
    private String authSecret;

    /**
     * 连续失败次数
     */
    private Integer failCount;

    /**
     * 最大失败次数
     */
    private Integer maxFailCount;

    /**
     * 锁定截止时间
     */
    private OffsetDateTime lockUntil;

    /**
     * 最后认证时间
     */
    private OffsetDateTime lastAuthTime;

    /**
     * 最后认证IP
     */
    private String lastAuthIp;

    /**
     * 状态
     */
    private String status;

    /**
     * 凭据过期时间
     */
    private OffsetDateTime expireTime;

    /**
     * 创建时间
     */
    private OffsetDateTime createTime;

    /**
     * 更新时间
     */
    private OffsetDateTime updateTime;
}