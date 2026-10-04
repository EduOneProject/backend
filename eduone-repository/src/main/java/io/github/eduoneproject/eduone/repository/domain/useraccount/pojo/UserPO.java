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
 * 用户持久化对象
 *
 * @author summerain0
 */
@Data
@Accessors(chain = true)
@TableName("bds_user")
public class UserPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    public static final String COLUMN_ID = "id";

    /**
     * 用户编号
     */
    public static final String COLUMN_USER_NO = "user_no";

    /**
     * 关联人员ID
     */
    public static final String COLUMN_PERSON_ID = "person_id";

    /**
     * 用户类型
     */
    public static final String COLUMN_USER_TYPE = "user_type";

    /**
     * 用户名
     */
    public static final String COLUMN_USER_NAME = "user_name";

    /**
     * 头像
     */
    public static final String COLUMN_AVATAR = "avatar";

    /**
     * 状态
     */
    public static final String COLUMN_STATUS = "status";

    /**
     * 最后登录时间
     */
    public static final String COLUMN_LAST_LOGIN_TIME = "last_login_time";

    /**
     * 最后登录IP
     */
    public static final String COLUMN_LAST_LOGIN_IP = "last_login_ip";

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
     * 用户ID
     */
    @TableId(value = COLUMN_ID)
    private String id;

    /**
     * 用户编号
     */
    @TableField(COLUMN_USER_NO)
    private String userNo;

    /**
     * 关联人员ID
     */
    @TableField(COLUMN_PERSON_ID)
    private String personId;

    /**
     * 用户类型
     */
    @TableField(COLUMN_USER_TYPE)
    private String userType;

    /**
     * 用户名
     */
    @TableField(COLUMN_USER_NAME)
    private String userName;

    /**
     * 头像
     */
    @TableField(COLUMN_AVATAR)
    private String avatar;

    /**
     * 状态
     */
    @TableField(COLUMN_STATUS)
    private String status;

    /**
     * 最后登录时间
     */
    @TableField(COLUMN_LAST_LOGIN_TIME)
    private OffsetDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @TableField(COLUMN_LAST_LOGIN_IP)
    private String lastLoginIp;

    /**
     * 创建人
     */
    @TableField(COLUMN_CREATE_USER_ID)
    private String createUserId;

    /**
     * 创建时间
     */
    @TableField(value = COLUMN_CREATE_TIME)
    private OffsetDateTime createTime;

    /**
     * 更新人
     */
    @TableField(COLUMN_UPDATE_USER_ID)
    private String updateUserId;

    /**
     * 更新时间
     */
    @TableField(value = COLUMN_UPDATE_TIME)
    private OffsetDateTime updateTime;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    @TableField(COLUMN_IS_DELETED)
    private Boolean deleted;
}
