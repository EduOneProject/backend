package io.github.eduoneproject.eduone.business.kernel.domain.usersession.agg;

import io.github.eduoneproject.eduone.business.common.support.UserAgentHelper;
import io.github.eduoneproject.eduone.business.domain.api.usersession.consts.UserSessionStatus;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.UserSessionCreatedEvent;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.UserSessionRevokedEvent;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.UserSessionTokenRotatedEvent;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.dto.UserAgentInfo;
import io.github.eduoneproject.eduone.business.kernel.domain.usersession.command.CreateUserSessionCommand;
import io.github.eduoneproject.eduone.business.kernel.domain.usersession.command.RevokeUserSessionCommand;
import io.github.eduoneproject.eduone.business.kernel.domain.usersession.command.RotateUserSessionTokenCommand;
import io.github.eduoneproject.eduone.business.kernel.domain.usersession.dto.UserAgentInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator;
import org.axonframework.extension.spring.stereotype.EventSourced;
import org.axonframework.messaging.commandhandling.annotation.CommandHandler;
import org.axonframework.messaging.eventhandling.gateway.EventAppender;

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

    /**
     * 处理创建用户会话命令（创建型命令处理器）
     *
     * @param command         创建用户会话命令
     * @param eventAppender   事件追加器
     * @param userAgentHelper 用户代理辅助类
     */
    @CommandHandler
    public static void handle(
            CreateUserSessionCommand command,
            EventAppender eventAppender,
            UserAgentHelper userAgentHelper
    ) {
        UserSessionCreatedEvent event = new UserSessionCreatedEvent();
        event.setId(command.getId());
        event.setUserId(command.getUserId());
        event.setStatus(UserSessionStatus.ACTIVE);
        UserAgentHelper.UserAgentInfo parseUserAgentInfo = userAgentHelper.parse(command.getUserAgent());
        if (parseUserAgentInfo != null) {
            UserAgentInfo eventUserAgentInfo = new UserAgentInfo();
            eventUserAgentInfo.setUserAgent(command.getUserAgent());
            eventUserAgentInfo.setDeviceClass(parseUserAgentInfo.deviceClass());
            event.setUserAgentInfo(eventUserAgentInfo);
        }
        event.setActiveTime(command.getActiveTime());
        eventAppender.append(event);
    }

    /**
     * 处理轮换用户会话令牌命令
     *
     * @param command       轮换用户会话令牌命令
     * @param eventAppender 事件追加器
     */
    @CommandHandler
    public void handle(RotateUserSessionTokenCommand command, EventAppender eventAppender) {
        UserSessionTokenRotatedEvent event = new UserSessionTokenRotatedEvent();
        event.setId(command.getId());
        event.setUserId(command.getUserId());
        event.setRotateTime(command.getRotateTime());
        eventAppender.append(event);
    }

    /**
     * 处理销毁用户会话命令
     *
     * @param command       销毁用户会话命令
     * @param eventAppender 事件追加器
     */
    @CommandHandler
    public void handle(RevokeUserSessionCommand command, EventAppender eventAppender) {
        UserSessionRevokedEvent event = new UserSessionRevokedEvent();
        event.setId(command.getId());
        event.setRevokeTime(command.getRevokeTime());
        eventAppender.append(event);
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

    @EventSourcingHandler
    public void on(UserSessionTokenRotatedEvent event) {
        this.id = event.getId();
        this.userId = event.getUserId();
    }

    @EventSourcingHandler
    public void on(UserSessionRevokedEvent event) {
        this.id = event.getId();
        this.status = UserSessionStatus.TERMINATED;
        this.terminateTime = event.getRevokeTime();
    }
}
