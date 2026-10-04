package io.github.eduoneproject.eduone.business.kernel.appservice.impl;

import io.github.eduoneproject.eduone.business.kernel.appservice.AuthAppService;
import io.github.eduoneproject.eduone.business.kernel.security.auth.AccountAuthenticator;
import io.github.eduoneproject.eduone.business.kernel.security.model.Principal;
import io.github.eduoneproject.eduone.business.kernel.security.model.PrincipalId;
import io.github.eduoneproject.eduone.business.kernel.service.TokenService;
import io.github.eduoneproject.eduone.business.kernel.service.model.LoginSuccessResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateRefreshTokenParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.LoginWithPasswordParam;
import io.github.eduoneproject.eduone.common.consts.CommonExceptionCode;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 认证业务类实现
 *
 * @author summerain0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthAppServiceImpl implements AuthAppService {
    private final AccountAuthenticator accountAuthenticator;
    private final TokenService tokenService;

    @Override
    public LoginSuccessResult loginWithPassword(LoginWithPasswordParam param) {
        if (StringUtils.isBlank(param.getAccount()) || StringUtils.isBlank(param.getPassword())) {
            throw new BasicRuntimeException(CommonExceptionCode.PARAM_REQUIRED.getCode(), "账号或密码为空");
        }
        Authentication authenticate = accountAuthenticator.authenticate(param.getAccount(), param.getPassword());
        Principal principal = (Principal) authenticate.getPrincipal();
        if (principal == null) {
            log.error("认证失败，principal为空");
            throw new BasicRuntimeException("认证失败");
        }

        // 生产token
        return generateLoginSuccessResult(principal);
    }

    /**
     * 生成登录成功结果
     *
     * @param principal 用户信息
     * @return 登录成功结果
     */
    private LoginSuccessResult generateLoginSuccessResult(Principal principal) {
        if (principal == null) {
            throw new BasicRuntimeException(CommonExceptionCode.INTERNAL_ERROR.getCode(), "认证失败");
        }
        PrincipalId principalId = principal.getId();
        String id = principalId.id();

        String accessToken = tokenService.createAccessToken(id);
        CreateRefreshTokenParam createRefreshTokenParam = new CreateRefreshTokenParam();
        createRefreshTokenParam.setUserId(id);
        String refreshToken = tokenService.createRefreshToken(createRefreshTokenParam);

        LoginSuccessResult result = new LoginSuccessResult();
        result.setUserId(id);
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);
        return result;
    }
}
