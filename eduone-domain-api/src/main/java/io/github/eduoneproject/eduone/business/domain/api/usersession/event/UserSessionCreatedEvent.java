package io.github.eduoneproject.eduone.business.domain.api.usersession.event;

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
     * 激活时间
     */
    private OffsetDateTime activeTime;
}
