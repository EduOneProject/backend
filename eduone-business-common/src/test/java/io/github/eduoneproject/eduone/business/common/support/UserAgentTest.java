package io.github.eduoneproject.eduone.business.common.support;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.junit.jupiter.api.Test;

/**
 * 用户代理测试类
 *
 * @author summerain0
 */
public class UserAgentTest {
    @Test
    public void testUserAgent() {
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/157.0.0.0 Safari/537.36 Edg/157.0.0.0";
        UserAgentAnalyzer userAgentAnalyzer = getUserAgentAnalyzer();

        UserAgent.ImmutableUserAgent immutableUserAgent = userAgentAnalyzer.parse(userAgent);

        System.out.printf("设备类型：%s\n", immutableUserAgent.get(UserAgent.DEVICE_CLASS).getValue());
        System.out.printf("设备名称：%s\n", immutableUserAgent.get(UserAgent.DEVICE_NAME).getValue());
        System.out.printf("设备品牌：%s\n", immutableUserAgent.get(UserAgent.DEVICE_BRAND).getValue());
        System.out.printf("操作系统：%s\n", immutableUserAgent.get(UserAgent.OPERATING_SYSTEM_NAME_VERSION).getValue());
        System.out.printf("客户端：%s\n", immutableUserAgent.get(UserAgent.AGENT_NAME_VERSION).getValue());
    }

    /**
     * 获取用户代理分析器
     *
     * @return 用户代理分析器
     */
    private UserAgentAnalyzer getUserAgentAnalyzer() {
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
