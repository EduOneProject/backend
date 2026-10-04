package io.github.eduoneproject.eduone.business.gateway.resolver;

import io.github.eduoneproject.eduone.business.gateway.request.LoginWithPasswordRequest;
import io.github.eduoneproject.eduone.business.gateway.response.CommonBusinessResponse;
import io.github.eduoneproject.eduone.business.gateway.response.UserLoginSuccessResponse;
import io.github.eduoneproject.eduone.business.kernel.appservice.AuthAppService;
import io.github.eduoneproject.eduone.business.kernel.service.model.LoginSuccessResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.LoginWithPasswordParam;
import lombok.RequiredArgsConstructor;
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
public class UserLoginMutationResolver {
    private final AuthAppService authAppService;

    /**
     * 以密码方式登录
     *
     * @param request 登录请求参数
     * @return 登录结果
     */
    @MutationMapping
    public CommonBusinessResponse<UserLoginSuccessResponse> loginWithPassword(@Argument LoginWithPasswordRequest request) {
        LoginWithPasswordParam param = new LoginWithPasswordParam();
        param.setAccount(request.getAccount());
        param.setPassword(request.getPassword());
        LoginSuccessResult loginSuccessResult = authAppService.loginWithPassword(param);
        UserLoginSuccessResponse response = new UserLoginSuccessResponse();
        response.setUserId(loginSuccessResult.getUserId());
        response.setAccessToken(loginSuccessResult.getAccessToken());
        response.setRefreshToken(loginSuccessResult.getRefreshToken());
        return CommonBusinessResponse.success(response);
    }
}
