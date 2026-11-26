package io.github.eduoneproject.eduone.business.domain.api.usersession.consts;

import io.github.eduoneproject.eduone.business.domain.api.usersession.event.UserSessionCreatedEvent;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.UserSessionRevokedEvent;
import lombok.Getter;

/**
 * 用户会话状态
 *
 * @author summerain0
 */
public enum UserSessionStatus {
    /**
     * 激活
     */
    ACTIVE("ACTIVE", "激活"),
    /**
     * 过期
     */
    EXPIRED("EXPIRED", "过期"),
    /**
     * 终止
     */
    TERMINATED("TERMINATED", "终止");

    /**
     * 编码
     */
    @Getter
    private final String code;

    /**
     * 描述
     */
    @Getter
    private final String description;

    /**
     * 枚举构造函数
     *
     * @param code        编码
     * @param description 描述
     */
    UserSessionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 投影支持类
     */
    public static class Projection {
        public static UserSessionStatus of(@SuppressWarnings("unused") UserSessionCreatedEvent event) {
            return ACTIVE;
        }

        public static UserSessionStatus of(@SuppressWarnings("unused") UserSessionRevokedEvent event) {
            return TERMINATED;
        }
    }
}
