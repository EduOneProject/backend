package io.github.eduoneproject.eduone.repository.domain.usersession.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 用户会话实体类
 *
 * @author summerain0
 */
@Data
@Accessors(chain = true)
@TableName("bds_user_session")
public class UserSessionPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    @TableId("sid")
    private String id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 设备指纹
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 设备信息
     */
    @TableField("device_info")
    private String deviceInfo;

    /**
     * 登录IP
     */
    @TableField("login_ip")
    private String loginIp;

    /**
     * 最后活跃IP
     */
    @TableField("last_active_ip")
    private String lastActiveIp;

    /**
     * 会话状态
     *
     * @see io.github.eduoneproject.eduone.business.domain.api.usersession.consts.UserSessionStatus
     */
    @TableField("status")
    private String status;

    /**
     * 最后活跃时间
     */
    @TableField("last_active_time")
    private OffsetDateTime lastActiveTime;

    /**
     * 登出时间
     */
    @TableField("logout_time")
    private OffsetDateTime logoutTime;

    /**
     * 登出原因
     */
    @TableField("logout_reason")
    private String logoutReason;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private OffsetDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private OffsetDateTime updatedTime;
}
