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
    public static final String COLUMN_ID = "id";

    /**
     * 关联用户ID
     */
    public static final String COLUMN_USER_ID = "user_id";

    /**
     * 注册渠道
     */
    public static final String COLUMN_CHANNEL = "channel";

    /**
     * 状态
     */
    public static final String COLUMN_STATUS = "status";

    /**
     * 账号过期时间
     */
    public static final String COLUMN_EXPIRE_TIME = "expire_time";

    /**
     * 锁定时间
     */
    public static final String COLUMN_LOCK_TIME = "lock_time";

    /**
     * 锁定原因
     */
    public static final String COLUMN_LOCK_REASON = "lock_reason";

    /**
     * 创建人
     */
    public static final String COLUMN_CREATE_USER_ID = "create_user_id";

    /**
     * 创建时间
     */
    public static final String COLUMN_CREATE_TIME = "create_time";

    /**
     * 更新人
     */
    public static final String COLUMN_UPDATE_USER_ID = "update_user_id";

    /**
     * 更新时间
     */
    public static final String COLUMN_UPDATE_TIME = "update_time";

    /**
     * 逻辑删除标识
     */
    public static final String COLUMN_IS_DELETED = "is_deleted";

    /**
     * 账号ID
     */
    @TableId(COLUMN_ID)
    private String id;

    /**
     * 关联用户ID
     */
    @TableField(COLUMN_USER_ID)
    private String userId;

    /**
     * 注册渠道
     */
    @TableField(COLUMN_CHANNEL)
    private String channel;

    /**
     * 状态
     */
    @TableField(COLUMN_STATUS)
    private String status;

    /**
     * 账号过期时间
     */
    @TableField(COLUMN_EXPIRE_TIME)
    private OffsetDateTime expireTime;

    /**
     * 锁定时间
     */
    @TableField(COLUMN_LOCK_TIME)
    private OffsetDateTime lockTime;

    /**
     * 锁定原因
     */
    @TableField(COLUMN_LOCK_REASON)
    private String lockReason;

    /**
     * 创建人
     */
    @TableField(COLUMN_CREATE_USER_ID)
    private String createUserId;

    /**
     * 创建时间
     */
    @TableField(COLUMN_CREATE_TIME)
    private OffsetDateTime createTime;

    /**
     * 更新人
     */
    @TableField(COLUMN_UPDATE_USER_ID)
    private String updateUserId;

    /**
     * 更新时间
     */
    @TableField(COLUMN_UPDATE_TIME)
    private OffsetDateTime updateTime;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    @TableField(COLUMN_IS_DELETED)
    private Integer deleted;
}