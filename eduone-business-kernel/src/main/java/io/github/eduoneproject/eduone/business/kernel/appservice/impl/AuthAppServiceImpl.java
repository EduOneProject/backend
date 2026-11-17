package io.github.eduoneproject.eduone.business.kernel.appservice.impl;

import io.github.eduoneproject.eduone.business.common.config.ExpirationConfig;
import io.github.eduoneproject.eduone.business.kernel.appservice.AuthAppService;
import io.github.eduoneproject.eduone.business.kernel.domain.usersession.command.CreateUserSessionCommand;
import io.github.eduoneproject.eduone.business.kernel.security.auth.AccountAuthenticator;
import io.github.eduoneproject.eduone.business.kernel.security.model.Principal;
import io.github.eduoneproject.eduone.business.kernel.service.TokenService;
import io.github.eduoneproject.eduone.business.kernel.service.model.LoginSuccessResult;
import io.github.eduoneproject.eduone.business.kernel.service.model.UserTokenResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateUserTokenParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.LoginWithPasswordParam;
import io.github.eduoneproject.eduone.common.consts.CommonExceptionCode;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.axonframework.messaging.commandhandling.gateway.CommandGateway;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

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
    private final CommandGateway commandGateway;
    private final ExpirationConfig expirationConfig;

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

        OffsetDateTime activeTime = OffsetDateTime.now();
        String sessionId = UUID.randomUUID().toString();
        String userId = principal.getId().id();
        String accountId = principal.getAccountId().id();

        // 生成token
        CreateUserTokenParam createUserTokenParam = new CreateUserTokenParam();
        createUserTokenParam.setSessionId(sessionId);
        createUserTokenParam.setUserId(userId);
        createUserTokenParam.setAccountId(accountId);
        createUserTokenParam.setIssuedAt(OffsetDateTime.now());
        createUserTokenParam.setExpirationTime(OffsetDateTime.now().plusSeconds(expirationConfig.getAccessToken().getSeconds()));
        UserTokenResult userTokenResult = tokenService.generateUserToken(createUserTokenParam);
        String accessToken = userTokenResult.getAccessToken();
        String refreshToken = userTokenResult.getRefreshToken();

        // 发送创建会话命令
        CreateUserSessionCommand command = new CreateUserSessionCommand();
        command.setId(sessionId);
        command.setUserId(userId);
        command.setActiveTime(activeTime);
        command.setUserAgent(param.getUserAgent());
        commandGateway.sendAndWait(command);

        LoginSuccessResult result = new LoginSuccessResult();
        result.setUserId(userId);
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);
        return result;
    }
}
