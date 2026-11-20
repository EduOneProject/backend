package io.github.eduoneproject.eduone.business.kernel.config.axon;

import io.github.eduoneproject.eduone.business.kernel.consts.AxonMetadataKey;
import io.github.eduoneproject.eduone.common.domain.context.ServiceContext;
import io.github.eduoneproject.eduone.common.domain.context.ServiceContextLocal;
import org.axonframework.messaging.commandhandling.CommandMessage;
import org.axonframework.messaging.core.MessageDispatchInterceptor;
import org.axonframework.messaging.core.MessageDispatchInterceptorChain;
import org.axonframework.messaging.core.MessageStream;
import org.axonframework.messaging.core.unitofwork.ProcessingContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 命令元数据拦截器
 *
 * @author summerain0
 */
@Component
public class CommandMetadataDispatchInterceptor implements MessageDispatchInterceptor<CommandMessage> {
    @NonNull
    @Override
    public MessageStream<?> interceptOnDispatch(
            @NonNull CommandMessage message,
            @Nullable ProcessingContext context,
            @NonNull MessageDispatchInterceptorChain<CommandMessage> interceptorChain
    ) {
        ServiceContext serviceContext = ServiceContextLocal.getServiceContext();
        if (serviceContext != null) {
            message = message.andMetadata(Map.of(AxonMetadataKey.SERVICE_CONTEXT, serviceContext.toJson()));
        }
        return interceptorChain.proceed(message, context);
    }
}
