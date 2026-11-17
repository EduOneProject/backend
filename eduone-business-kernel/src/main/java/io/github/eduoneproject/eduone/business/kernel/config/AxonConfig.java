package io.github.eduoneproject.eduone.business.kernel.config;

import org.axonframework.messaging.commandhandling.CommandBus;
import org.axonframework.messaging.commandhandling.CommandPriorityCalculator;
import org.axonframework.messaging.commandhandling.RoutingStrategy;
import org.axonframework.messaging.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.messaging.core.MessageTypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Axon配置
 *
 * @author summerain0
 */
@Configuration
public class AxonConfig {
    @Bean
    public CommandGateway commandGateway(org.axonframework.common.configuration.Configuration configuration) {
        return new DefaultCommandGateway(
                configuration.getComponent(CommandBus.class),
                configuration.getComponent(MessageTypeResolver.class),
                configuration.getComponent(CommandPriorityCalculator.class),
                configuration.getComponent(RoutingStrategy.class)
        );
    }
}
