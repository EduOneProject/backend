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
 * 人员信息表
 *
 * @author summerain0
 */
@Data
@Accessors(chain = true)
@TableName("bds_person")
public class PersonPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 人员ID
     */
    public static final String COLUMN_ID = "id";

    /**
     * 人员编号
     */
    public static final String COLUMN_PERSON_NO = "person_no";

    /**
     * 真实姓名
     */
    public static final String COLUMN_REAL_NAME = "real_name";

    /**
     * 昵称
     */
    public static final String COLUMN_NICK_NAME = "nick_name";

    /**
     * 性别
     */
    public static final String COLUMN_GENDER = "gender";

    /**
     * 手机号
     */
    public static final String COLUMN_MOBILE = "mobile";

    /**
     * 邮箱
     */
    public static final String COLUMN_EMAIL = "email";

    /**
     * 备注
     */
    public static final String COLUMN_REMARK = "remark";

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
     * 人员ID
     */
    @TableId(COLUMN_ID)
    private String id;

    /**
     * 人员编号
     */
    @TableField(COLUMN_PERSON_NO)
    private String personNo;

    /**
     * 真实姓名
     */
    @TableField(COLUMN_REAL_NAME)
    private String realName;

    /**
     * 昵称
     */
    @TableField(COLUMN_NICK_NAME)
    private String nickName;

    /**
     * 性别
     */
    @TableField(COLUMN_GENDER)
    private String gender;

    /**
     * 手机号
     */
    @TableField(COLUMN_MOBILE)
    private String mobile;

    /**
     * 邮箱
     */
    @TableField(COLUMN_EMAIL)
    private String email;

    /**
     * 备注
     */
    @TableField(COLUMN_REMARK)
    private String remark;

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
    private Boolean deleted;
}