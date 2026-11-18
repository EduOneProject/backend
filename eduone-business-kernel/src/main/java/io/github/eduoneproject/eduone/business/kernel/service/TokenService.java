package io.github.eduoneproject.eduone.business.kernel.service;

import io.github.eduoneproject.eduone.business.kernel.service.model.AccessTokenJwtPayload;
import io.github.eduoneproject.eduone.business.kernel.service.model.RotateAccessTokenResult;
import io.github.eduoneproject.eduone.business.kernel.service.model.UserTokenResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateAccessTokenParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateRefreshTokenParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateUserTokenParam;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Token业务接口
 *
 * @author summerain0
 */
public interface TokenService {
    /**
     * 生成用户令牌
     *
     * @param param 创建参数
     * @return 用户令牌
     */
    UserTokenResult generateUserToken(CreateUserTokenParam param);

    /**
     * 创建访问令牌
     *
     * @param param 创建参数
     * @return 访问令牌
     */
    String createAccessToken(CreateAccessTokenParam param);

    /**
     * 创建刷新令牌
     *
     * @param param 创建参数
     * @return 刷新令牌
     */
    String createRefreshToken(CreateRefreshTokenParam param);

    /**
     * 创建刷新令牌，可以指定刷新令牌的值
     *
     * @param refreshToken 刷新令牌
     * @param param        创建参数
     * @return 刷新令牌
     */
    String createRefreshToken(String refreshToken, CreateRefreshTokenParam param);

    /**
     * 解析访问令牌
     *
     * @param accessToken 访问令牌
     * @return 令牌信息
     */
    AccessTokenJwtPayload parseAccessToken(String accessToken);

    /**
     * 从请求中提取令牌
     *
     * @param request HTTP请求对象
     * @return 令牌信息
     */
    AccessTokenJwtPayload extractToken(HttpServletRequest request);

    /**
     * 轮换访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 轮换结果
     */
    RotateAccessTokenResult rotateAccessToken(String refreshToken);
}
