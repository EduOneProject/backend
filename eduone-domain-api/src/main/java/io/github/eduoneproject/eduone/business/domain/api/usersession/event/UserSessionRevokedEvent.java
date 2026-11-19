package io.github.eduoneproject.eduone.business.domain.api.usersession.event;

import lombok.Data;
import org.axonframework.eventsourcing.annotation.EventTag;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 用户会话已销毁事件
 *
 * @author summerain0
 */
@Data
public class UserSessionRevokedEvent implements Serializable {
    /**
     * 会话ID
     */
    @EventTag(key = "UserSessionAgg")
    private String id;

    /**
     * 销毁时间
     */
    private OffsetDateTime revokeTime;
}
