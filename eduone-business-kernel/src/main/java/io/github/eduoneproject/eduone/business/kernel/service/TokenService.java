package io.github.eduoneproject.eduone.business.kernel.service;

import io.github.eduoneproject.eduone.business.kernel.service.param.CreateRefreshTokenParam;
import io.jsonwebtoken.Claims;

/**
 * Token业务接口
 *
 * @author summerain0
 */
public interface TokenService {
    /**
     * 创建访问令牌
     *
     * @param userId 用户ID
     * @return 访问令牌
     */
    String createAccessToken(String userId);

    /**
     * 创建刷新令牌
     *
     * @param param 创建参数
     * @return 刷新令牌
     */
    String createRefreshToken(CreateRefreshTokenParam param);

    /**
     * 解析访问令牌
     *
     * @param accessToken 访问令牌
     * @return 令牌信息
     */
    Claims parseAccessToken(String accessToken);
}
