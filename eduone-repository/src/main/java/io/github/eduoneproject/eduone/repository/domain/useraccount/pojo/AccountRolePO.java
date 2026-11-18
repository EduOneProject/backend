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
    @TableId("id")
    private String id;

    /**
     * 账号ID
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private String roleId;

    /**
     * 授权人
     */
    @TableField("grant_by")
    private String grantBy;

    /**
     * 授权时间
     */
    @TableField("grant_time")
    private OffsetDateTime grantTime;

    /**
     * 授权过期时间
     */
    @TableField("expire_time")
    private OffsetDateTime expireTime;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

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