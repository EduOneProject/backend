package io.github.eduoneproject.eduone.business.gateway.resolver;

import io.github.eduoneproject.eduone.business.gateway.request.LoginWithPasswordRequest;
import io.github.eduoneproject.eduone.business.gateway.response.CommonBusinessResponse;
import io.github.eduoneproject.eduone.business.gateway.response.UserLoginSuccessResponse;
import io.github.eduoneproject.eduone.business.kernel.appservice.AuthAppService;
import io.github.eduoneproject.eduone.business.kernel.service.model.LoginSuccessResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.LoginWithPasswordParam;
import io.github.eduoneproject.eduone.common.annotation.OptionalLogin;
import io.github.eduoneproject.eduone.common.domain.context.HttpIdentity;
import io.github.eduoneproject.eduone.common.domain.context.ServiceContext;
import io.github.eduoneproject.eduone.common.domain.context.ServiceContextLocal;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

/**
 * 用户登录接口
 *
 * @author summerain0
 */
@RequiredArgsConstructor
@Controller
public class UserLoginMutationResolver {
    private final AuthAppService authAppService;

    /**
     * 以密码方式登录
     *
     * @param request 登录请求参数
     * @return 登录结果
     */
    @OptionalLogin
    @MutationMapping
    public CommonBusinessResponse<UserLoginSuccessResponse> loginWithPassword(@Argument LoginWithPasswordRequest request) {
        String userAgent = Optional.ofNullable(ServiceContextLocal.getServiceContext())
                .map(ServiceContext::getHttpIdentity)
                .map(HttpIdentity::getUserAgent)
                .orElse(null);

        LoginWithPasswordParam param = new LoginWithPasswordParam();
        param.setAccount(request.getAccount());
        param.setPassword(request.getPassword());
        param.setUserAgent(userAgent);
        LoginSuccessResult loginSuccessResult = authAppService.loginWithPassword(param);
        UserLoginSuccessResponse response = new UserLoginSuccessResponse();
        response.setUserId(loginSuccessResult.getUserId());
        response.setAccessToken(loginSuccessResult.getAccessToken());
        response.setRefreshToken(loginSuccessResult.getRefreshToken());
        return CommonBusinessResponse.success(response);
    }
}
