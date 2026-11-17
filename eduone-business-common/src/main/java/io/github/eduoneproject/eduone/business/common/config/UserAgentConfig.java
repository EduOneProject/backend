package io.github.eduoneproject.eduone.business.common.config;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户代理配置类
 *
 * @author summerain0
 */
@Configuration
public class UserAgentConfig {
    /**
     * 用户代理分析器
     *
     * @return 用户代理分析器
     */
    @Bean
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer.newBuilder()
                .withCache(2000)
                .withField(UserAgent.DEVICE_CLASS)
                .withField(UserAgent.DEVICE_NAME)
                .withField(UserAgent.DEVICE_BRAND)
                .withField(UserAgent.OPERATING_SYSTEM_NAME_VERSION)
                .withField(UserAgent.AGENT_NAME_VERSION)
                .withField(UserAgent.AGENT_CLASS)
                .build();
    }
}