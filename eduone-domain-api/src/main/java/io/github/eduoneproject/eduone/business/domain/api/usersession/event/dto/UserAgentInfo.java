package io.github.eduoneproject.eduone.business.domain.api.usersession.event.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户代理信息
 *
 * @author summerain0
 */
@Data
public class UserAgentInfo implements Serializable {
    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 设备类型
     */
    private String deviceClass;
}
