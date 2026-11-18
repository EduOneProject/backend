package io.github.eduoneproject.eduone.business.gateway.resolver;

import io.github.eduoneproject.eduone.business.gateway.response.CommonBusinessResponse;
import io.github.eduoneproject.eduone.business.gateway.response.RotateAccessTokenResponse;
import io.github.eduoneproject.eduone.business.kernel.service.TokenService;
import io.github.eduoneproject.eduone.business.kernel.service.model.RotateAccessTokenResult;
import io.github.eduoneproject.eduone.common.annotation.OptionalLogin;
import io.github.eduoneproject.eduone.common.consts.CommonExceptionCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * 用户登录接口
 *
 * @author summerain0
 */
@RequiredArgsConstructor
@Controller
public class AuthMutationResolver {
    private final TokenService tokenService;

    /**
     * 轮换访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 访问令牌和刷新令牌
     */
    @OptionalLogin
    @MutationMapping
    public CommonBusinessResponse<RotateAccessTokenResponse> rotateAccessToken(@Argument String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            return CommonBusinessResponse.failure(CommonExceptionCode.PARAM_REQUIRED, "刷新令牌不能为空");
        }
        RotateAccessTokenResult rotateAccessTokenResult = tokenService.rotateAccessToken(refreshToken);
        RotateAccessTokenResult.ResultCode resultCode = rotateAccessTokenResult.getResultCode();

        if (resultCode == RotateAccessTokenResult.ResultCode.SUCCESS) { // 正常轮换
            RotateAccessTokenResponse response = new RotateAccessTokenResponse();
            response.setAccessToken(rotateAccessTokenResult.getAccessToken());
            response.setRefreshToken(rotateAccessTokenResult.getRefreshToken());
            return CommonBusinessResponse.success(response);
        } else if (resultCode == RotateAccessTokenResult.ResultCode.CONFLICT_IN_GRACE_WINDOW) { // 宽限期轮换
            return CommonBusinessResponse.failure(CommonExceptionCode.CONTINUE, "宽限期轮换，请继续使用当前令牌");
        } else { // 其他情况
            return CommonBusinessResponse.failure(CommonExceptionCode.UNAUTHORIZED, null);
        }
    }
}
