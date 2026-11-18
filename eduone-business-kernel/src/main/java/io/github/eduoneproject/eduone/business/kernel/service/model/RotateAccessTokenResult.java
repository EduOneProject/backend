package io.github.eduoneproject.eduone.business.kernel.service.model;

import lombok.Data;

import java.io.Serializable;

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
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 访问令牌
     */
    private String accessToken;

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
     * 轮换成功
     *
     * @param refreshToken 刷新令牌
     * @param accessToken  访问令牌
     * @return 轮换结果
     */
    public static RotateAccessTokenResult success(String refreshToken, String accessToken) {
        RotateAccessTokenResult result = new RotateAccessTokenResult();
        result.setResultCode(ResultCode.SUCCESS);
        result.setRefreshToken(refreshToken);
        result.setAccessToken(accessToken);
        return result;
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
