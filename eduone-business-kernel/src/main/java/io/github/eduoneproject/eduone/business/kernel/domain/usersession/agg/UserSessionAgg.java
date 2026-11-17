package io.github.eduoneproject.eduone.business.kernel.domain.usersession.agg;

import io.github.eduoneproject.eduone.business.domain.api.usersession.consts.UserSessionStatus;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.UserSessionCreatedEvent;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.dto.UserAgentInfo;
import io.github.eduoneproject.eduone.business.kernel.domain.usersession.dto.UserAgentInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator;
import org.axonframework.extension.spring.stereotype.EventSourced;

import java.time.OffsetDateTime;

/**
 * 用户会话聚合根
 *
 * @author summerain0
 */
@Slf4j
@EventSourced(tagKey = "UserSessionAgg")
public class UserSessionAgg {
    /**
     * 会话ID
     */
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
    private UserAgentInfoDto userAgentInfo;

    /**
     * 激活时间
     */
    private OffsetDateTime activeTime;

    /**
     * 过期时间
     */
    private OffsetDateTime expireTime;

    /**
     * 终止时间
     */
    private OffsetDateTime terminateTime;

    @EntityCreator
    public UserSessionAgg() {
    }

    @EventSourcingHandler
    public void on(UserSessionCreatedEvent event) {
        this.id = event.getId();
        this.userId = event.getUserId();
        this.status = event.getStatus();
        UserAgentInfo eventUserAgentInfo = event.getUserAgentInfo();
        if (eventUserAgentInfo != null) {
            this.userAgentInfo = new UserAgentInfoDto();
            this.userAgentInfo.setUserAgent(eventUserAgentInfo.getUserAgent());
            this.userAgentInfo.setDeviceClass(eventUserAgentInfo.getDeviceClass());
        } else {
            this.userAgentInfo = null;
        }
        this.activeTime = event.getActiveTime();
    }
}
