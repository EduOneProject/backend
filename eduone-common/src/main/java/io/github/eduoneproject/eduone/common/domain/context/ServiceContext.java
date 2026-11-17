package io.github.eduoneproject.eduone.common.domain.context;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
}
