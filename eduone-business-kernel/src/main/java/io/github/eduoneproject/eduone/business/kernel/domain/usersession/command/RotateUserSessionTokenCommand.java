package io.github.eduoneproject.eduone.business.kernel.domain.usersession.command;

import lombok.Data;
import org.axonframework.messaging.commandhandling.annotation.Command;
import org.axonframework.modelling.annotation.TargetEntityId;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 轮换用户会话令牌命令
 *
 * @author summerain0
 */
@Data
@Command(routingKey = "id")
public class RotateUserSessionTokenCommand implements Serializable {
    /**
     * 会话ID
     */
    @TargetEntityId
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 轮换时间
     */
    private OffsetDateTime rotateTime;
}
