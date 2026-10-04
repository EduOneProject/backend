package io.github.eduoneproject.eduone.business.kernel.service.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建刷新令牌参数
 *
 * @author summerain0
 */
@Data
public class CreateRefreshTokenParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * IP地址
     */
    private String ipAddress;
}
