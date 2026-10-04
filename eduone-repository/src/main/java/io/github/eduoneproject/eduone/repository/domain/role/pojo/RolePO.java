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
    public static final String COLUMN_ID = "id";

    /**
     * 角色编码
     */
    public static final String COLUMN_ROLE_CODE = "role_code";

    /**
     * 角色名称
     */
    public static final String COLUMN_ROLE_NAME = "role_name";

    /**
     * 角色类型
     */
    public static final String COLUMN_ROLE_TYPE = "role_type";

    /**
     * 数据范围
     */
    public static final String COLUMN_DATA_SCOPE = "data_scope";

    /**
     * 排序
     */
    public static final String COLUMN_SORT = "sort";

    /**
     * 状态
     */
    public static final String COLUMN_STATUS = "status";

    /**
     * 描述
     */
    public static final String COLUMN_DESCRIPTION = "description";

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
     * 角色ID
     */
    @TableId(COLUMN_ID)
    private String id;

    /**
     * 角色编码
     */
    @TableField(COLUMN_ROLE_CODE)
    private String roleCode;

    /**
     * 角色名称
     */
    @TableField(COLUMN_ROLE_NAME)
    private String roleName;

    /**
     * 角色类型
     */
    @TableField(COLUMN_ROLE_TYPE)
    private String roleType;

    /**
     * 数据范围
     */
    @TableField(COLUMN_DATA_SCOPE)
    private String dataScope;

    /**
     * 排序
     */
    @TableField(COLUMN_SORT)
    private Integer sort;

    /**
     * 状态
     */
    @TableField(COLUMN_STATUS)
    private String status;

    /**
     * 描述
     */
    @TableField(COLUMN_DESCRIPTION)
    private String description;

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