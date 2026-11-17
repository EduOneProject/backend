package io.github.eduoneproject.eduone.business.kernel.domain.usersession.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户代理信息
 *
 * @author summerain0
 */
@Data
public class UserAgentInfoDto implements Serializable {
    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 设备类型
     */
    private String deviceClass;
}
