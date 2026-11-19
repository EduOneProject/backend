package io.github.eduoneproject.eduone.business.kernel.service.model;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 轮换访问令牌结果
 *
 * @author summerain0
 */
@Data
public class RotateAccessTokenResult implements Serializable {
    /**
     * 轮换结果
     */
    private ResultCode resultCode;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 轮换时间
     */
    private OffsetDateTime rotateTime;

    /**
     * 结果码
     */
    public enum ResultCode {
        /**
         * 轮换成功
         */
        SUCCESS,

        /**
         * 并发轮换冲突，处于宽限期内
         */
        CONFLICT_IN_GRACE_WINDOW,

        /**
         * 重放攻击
         */
        REPLAY_ATTACK,

        /**
         * token已被吊销
         */
        TOKEN_REVOKED,

        /**
         * token已过期
         */
        TOKEN_EXPIRED,

        /**
         * token不存在
         */
        TOKEN_NOT_FOUND
    }

    /**
     * 轮换失败
     *
     * @param resultCode 结果码
     * @return 轮换结果
     */
    public static RotateAccessTokenResult failure(ResultCode resultCode) {
        RotateAccessTokenResult result = new RotateAccessTokenResult();
        result.setResultCode(resultCode);
        return result;
    }
}
