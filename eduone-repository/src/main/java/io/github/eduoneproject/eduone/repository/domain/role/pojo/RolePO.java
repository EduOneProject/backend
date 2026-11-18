package io.github.eduoneproject.eduone.repository.domain.role.pojo;

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
 * 角色表
 *
 * @author summerain0
 */
@Data
@Accessors(chain = true)
@TableName("bds_role")
public class RolePO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId("id")
    private String id;

    /**
     * 角色编码
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 角色类型
     */
    @TableField("role_type")
    private String roleType;

    /**
     * 数据范围
     */
    @TableField("data_scope")
    private String dataScope;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

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