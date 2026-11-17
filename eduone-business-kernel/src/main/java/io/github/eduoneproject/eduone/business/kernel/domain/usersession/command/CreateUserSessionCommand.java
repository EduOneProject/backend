package io.github.eduoneproject.eduone.business.kernel.domain.usersession.command;

import lombok.Data;
import org.axonframework.messaging.commandhandling.annotation.Command;
import org.axonframework.modelling.annotation.TargetEntityId;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 创建用户会话命令
 *
 * @author summerain0
 */
@Data
@Command(routingKey = "id")
public class CreateUserSessionCommand implements Serializable {
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
     * 激活时间
     */
    private OffsetDateTime activeTime;

    /**
     * 用户代理
     */
    private String userAgent;
}
