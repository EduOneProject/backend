package io.github.eduoneproject.eduone.business.domain.api.usersession.event;

import io.github.eduoneproject.eduone.business.domain.api.usersession.consts.UserSessionStatus;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.dto.UserAgentInfo;
import lombok.Data;
import org.axonframework.eventsourcing.annotation.EventTag;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 用户会话已创建事件
 *
 * @author summerain0
 */
@Data
public class UserSessionCreatedEvent implements Serializable {
    /**
     * 会话ID
     */
    @EventTag(key = "UserSessionAgg")
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 会话状态
     */
    private UserSessionStatus status;

    /**
     * 用户代理
     */
    private UserAgentInfo userAgentInfo;

    /**
     * 激活时间
     */
    private OffsetDateTime activeTime;
}
