package io.github.eduoneproject.eduone.common.domain.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户标识信息
 *
 * @author summerain0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIdentity implements Serializable {
    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 用户ID
     */
    private String userId;
}
