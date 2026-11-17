package io.github.eduoneproject.eduone.business.common.support;

import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.AgentField;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 用户代理标识支持类
 *
 * @author summerain0
 */
@RequiredArgsConstructor
@Component
public class UserAgentHelper {
    private final UserAgentAnalyzer userAgentAnalyzer;

    /**
     * 解析用户代理字符串并返回用户代理信息。
     *
     * @param rawUserAgent 原始用户代理字符串
     * @return 用户代理信息
     */
    public UserAgentInfo parse(String rawUserAgent) {
        if (!StringUtils.isNotBlank(rawUserAgent)) {
            return UserAgentInfo.empty();
        }
        UserAgent ua = userAgentAnalyzer.parse(rawUserAgent);
        return new UserAgentInfo(
                clean(ua.get(UserAgent.DEVICE_CLASS)),
                clean(ua.get(UserAgent.DEVICE_NAME)),
                clean(ua.get(UserAgent.DEVICE_BRAND)),
                clean(ua.get(UserAgent.OPERATING_SYSTEM_NAME_VERSION)),
                clean(ua.get(UserAgent.AGENT_NAME_VERSION)),
                clean(ua.get(UserAgent.AGENT_CLASS))
        );
    }

    /**
     * 清理无效值，避免审计表里出现大量 "Unknown"
     *
     * @param agentField 代理字段
     * @return 清理后的值
     */
    private String clean(AgentField agentField) {
        if (agentField == null || agentField.isDefaultValue()) {
            return null;
        }
        return agentField.getValue();
    }

    /**
     * 用户代理解析信息。
     *
     * @param deviceClass      设备类别
     * @param deviceName       设备名称/型号
     * @param deviceBrand      设备品牌
     * @param osNameVersion    操作系统名称及版本
     * @param agentNameVersion 客户端代理名称及版本
     * @param agentClass       客户端代理类别
     */
    public record UserAgentInfo(
            String deviceClass,
            String deviceName,
            String deviceBrand,
            String osNameVersion,
            String agentNameVersion,
            String agentClass
    ) {
        /**
         * 返回一个空的用户代理解析信息。
         *
         * @return 空的用户代理解析信息
         */
        public static UserAgentInfo empty() {
            return new UserAgentInfo(null, null, null, null, null, null);
        }
    }
}
