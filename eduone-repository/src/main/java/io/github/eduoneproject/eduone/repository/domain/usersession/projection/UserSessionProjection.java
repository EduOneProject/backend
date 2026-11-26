package io.github.eduoneproject.eduone.repository.domain.usersession.projection;

import io.github.eduoneproject.eduone.business.domain.api.usersession.consts.UserSessionStatus;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.UserSessionCreatedEvent;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.UserSessionRevokedEvent;
import io.github.eduoneproject.eduone.business.domain.api.usersession.event.UserSessionTokenRotatedEvent;
import io.github.eduoneproject.eduone.common.domain.context.HttpIdentity;
import io.github.eduoneproject.eduone.common.domain.context.ServiceContext;
import io.github.eduoneproject.eduone.common.util.AxonMetaDataUtils;
import io.github.eduoneproject.eduone.repository.domain.usersession.pojo.UserSessionPO;
import io.github.eduoneproject.eduone.repository.domain.usersession.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.eventhandling.EventMessage;
import org.axonframework.messaging.eventhandling.annotation.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 用户会话投影
 *
 * @author summerain0
 */
@RequiredArgsConstructor
@Component
public class UserSessionProjection {
    private final UserSessionRepository userSessionRepository;

    @EventHandler
    public void on(UserSessionCreatedEvent event, EventMessage eventMessage) {
        ServiceContext serviceContext = AxonMetaDataUtils.getServiceContext(eventMessage.metadata());
        String ipAddress = Optional.ofNullable(serviceContext)
                .map(ServiceContext::getHttpIdentity)
                .map(HttpIdentity::getIpAddress)
                .orElse(null);
        String userAgent = Optional.ofNullable(serviceContext)
                .map(ServiceContext::getHttpIdentity)
                .map(HttpIdentity::getUserAgent)
                .orElse(null);

        UserSessionPO entity = new UserSessionPO();
        entity.setId(event.getId());
        entity.setUserId(event.getUserId());
        entity.setDeviceInfo(userAgent);
        entity.setLoginIp(ipAddress);
        entity.setLastActiveIp(ipAddress);
        entity.setStatus(UserSessionStatus.Projection.of(event).getCode());
        entity.setLastActiveTime(event.getActiveTime());
        entity.setCreatedTime(event.getActiveTime());
        entity.setUpdatedTime(event.getActiveTime());
        userSessionRepository.insert(entity);
    }

    @EventHandler
    public void on(UserSessionTokenRotatedEvent event) {
        UserSessionPO entity = new UserSessionPO();
        entity.setId(event.getId());
        entity.setUserId(event.getUserId());
        entity.setLastActiveTime(event.getRotateTime());
        entity.setUpdatedTime(event.getRotateTime());
        userSessionRepository.insertOrUpdate(entity);
    }

    @EventHandler
    public void on(UserSessionRevokedEvent event) {
        UserSessionPO entity = new UserSessionPO();
        entity.setId(event.getId());
        entity.setStatus(UserSessionStatus.Projection.of(event).getCode());
        entity.setLastActiveTime(event.getRevokeTime());
        entity.setUpdatedTime(event.getRevokeTime());
        userSessionRepository.insertOrUpdate(entity);
    }
}
