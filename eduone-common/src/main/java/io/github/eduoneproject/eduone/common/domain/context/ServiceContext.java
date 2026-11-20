package io.github.eduoneproject.eduone.common.domain.context;

import io.github.eduoneproject.eduone.common.util.SpringUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import tools.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.Objects;

/**
 * 服务上下文
 *
 * @author summerain0
 */
@Data
public class ServiceContext implements Serializable {
    /**
     * 用户标识信息
     */
    private UserIdentity userIdentity;

    /**
     * http标识信息
     */
    private HttpIdentity httpIdentity;

    /**
     * 判断上下文中是否有用户ID
     */
    public boolean hasUserId() {
        if (userIdentity == null) {
            return false;
        }
        return StringUtils.isNotBlank(userIdentity.getUserId()) && !Objects.equals(userIdentity.getUserId(), "-1");
    }

    /**
     * 获取用户ID
     */
    public String getUserId() {
        if (hasUserId()) {
            return userIdentity.getUserId();
        }
        return null;
    }

    /**
     * 当前服务上下文转换为json字符串
     */
    public String toJson() {
        ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
        return objectMapper.writeValueAsString(this);
    }

    /**
     * 从json字符串转换为服务上下文
     */
    public static ServiceContext fromJson(String json) {
        ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
        return objectMapper.readValue(json, ServiceContext.class);
    }
}
