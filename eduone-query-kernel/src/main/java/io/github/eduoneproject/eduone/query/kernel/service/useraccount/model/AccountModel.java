package io.github.eduoneproject.eduone.query.kernel.service.useraccount.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 账号信息
 *
 * @author summerain0
 */
@Data
public class AccountModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账号ID
     */
    private String id;

    /**
     * 关联用户ID
     */
    private String userId;

    /**
     * 注册渠道
     */
    private String channel;

    /**
     * 状态
     */
    private String status;

    /**
     * 账号过期时间
     */
    private OffsetDateTime expireTime;

    /**
     * 锁定时间
     */
    private OffsetDateTime lockTime;

    /**
     * 锁定原因
     */
    private String lockReason;

    /**
     * 创建人
     */
    private String createUserId;

    /**
     * 创建时间
     */
    private OffsetDateTime createTime;

    /**
     * 更新人
     */
    private String updateUserId;

    /**
     * 更新时间
     */
    private OffsetDateTime updateTime;
}