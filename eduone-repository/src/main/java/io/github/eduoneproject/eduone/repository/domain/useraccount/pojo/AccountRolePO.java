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
 * 账号角色关联表
 *
 * @author summerain0
 */
@Data
@Accessors(chain = true)
@TableName("bds_account_role")
public class AccountRolePO implements Serializable {
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
     * 角色ID
     */
    public static final String COLUMN_ROLE_ID = "role_id";

    /**
     * 授权人
     */
    public static final String COLUMN_GRANT_BY = "grant_by";

    /**
     * 授权时间
     */
    public static final String COLUMN_GRANT_TIME = "grant_time";

    /**
     * 授权过期时间
     */
    public static final String COLUMN_EXPIRE_TIME = "expire_time";

    /**
     * 状态
     */
    public static final String COLUMN_STATUS = "status";

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
     * 角色ID
     */
    @TableField(COLUMN_ROLE_ID)
    private String roleId;

    /**
     * 授权人
     */
    @TableField(COLUMN_GRANT_BY)
    private String grantBy;

    /**
     * 授权时间
     */
    @TableField(COLUMN_GRANT_TIME)
    private OffsetDateTime grantTime;

    /**
     * 授权过期时间
     */
    @TableField(COLUMN_EXPIRE_TIME)
    private OffsetDateTime expireTime;

    /**
     * 状态
     */
    @TableField(COLUMN_STATUS)
    private String status;

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