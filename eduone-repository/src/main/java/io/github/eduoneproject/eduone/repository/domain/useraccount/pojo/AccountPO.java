package io.github.eduoneproject.eduone.repository.domain.useraccount.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 账号信息表
 *
 * @author summerain0
 */
@Data
@Accessors(chain = true)
@TableName("bds_account")
public class AccountPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账号ID
     */
    @TableId("id")
    private String id;

    /**
     * 关联用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 注册渠道
     */
    @TableField("channel")
    private String channel;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 账号过期时间
     */
    @TableField("expire_time")
    private OffsetDateTime expireTime;

    /**
     * 锁定时间
     */
    @TableField("lock_time")
    private OffsetDateTime lockTime;

    /**
     * 锁定原因
     */
    @TableField("lock_reason")
    private String lockReason;

    /**
     * 创建人
     */
    @TableField("create_user_id")
    private String createUserId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private OffsetDateTime createTime;

    /**
     * 更新人
     */
    @TableField("update_user_id")
    private String updateUserId;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private OffsetDateTime updateTime;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;
}