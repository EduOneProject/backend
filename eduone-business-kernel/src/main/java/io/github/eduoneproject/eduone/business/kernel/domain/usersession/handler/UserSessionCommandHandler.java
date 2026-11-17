package io.github.eduoneproject.eduone.business.kernel.domain.usersession.handler;

import io.github.eduoneproject.eduone.business.common.support.UserAgentHelper;
import io.github.eduoneproject.eduone.business.domain.api.usersession.consts.UserSessionStatus;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.UserSessionCreatedEvent;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.dto.UserAgentInfo;
import io.github.eduoneproject.eduone.business.kernel.domain.usersession.command.CreateUserSessionCommand;
import org.axonframework.messaging.commandhandling.annotation.CommandHandler;
import org.axonframework.messaging.eventhandling.gateway.EventAppender;
import org.springframework.stereotype.Component;

/**
 * 用户会话命令处理器
 *
 * @author summerain0
 */
@Component
public class UserSessionCommandHandler {
    /**
     * 处理创建用户会话命令
     *
     * @param command         创建用户会话命令
     * @param eventAppender   事件追加器
     * @param userAgentHelper 用户代理辅助类
     */
    @CommandHandler
    public void handle(
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
}
