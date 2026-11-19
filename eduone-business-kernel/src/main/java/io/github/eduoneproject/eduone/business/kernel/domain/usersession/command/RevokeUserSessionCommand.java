package io.github.eduoneproject.eduone.business.kernel.domain.usersession.command;

import lombok.Data;
import org.axonframework.messaging.commandhandling.annotation.Command;
import org.axonframework.modelling.annotation.TargetEntityId;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 销毁用户会话命令
 *
 * @author summerain0
 */
@Data
@Command(routingKey = "id")
public class RevokeUserSessionCommand implements Serializable {
    /**
     * 会话ID
     */
    @TargetEntityId
    private String id;

    /**
     * 销毁时间
     */
    private OffsetDateTime revokeTime;
}
