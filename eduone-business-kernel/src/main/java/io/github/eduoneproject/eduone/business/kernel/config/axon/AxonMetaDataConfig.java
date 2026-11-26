package io.github.eduoneproject.eduone.business.kernel.config.axon;

import io.github.eduoneproject.eduone.common.consts.AxonMetadataKey;
import org.axonframework.messaging.core.correlation.SimpleCorrelationDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Axon元数据配置
 *
 * @author summerain0
 */
@Configuration
public class AxonMetaDataConfig {
    /**
     * 元数据提供者
     */
    @Bean
    public SimpleCorrelationDataProvider correlationProvider() {
        return new SimpleCorrelationDataProvider(
                AxonMetadataKey.SERVICE_CONTEXT
        );
    }
}