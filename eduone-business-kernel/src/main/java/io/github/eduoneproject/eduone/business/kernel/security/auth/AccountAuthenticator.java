package io.github.eduoneproject.eduone.business.kernel.security.auth;

import io.github.eduoneproject.eduone.business.common.consts.BusinessExceptionCode;
import io.github.eduoneproject.eduone.business.domain.api.account.consts.AccountAuthStatus;
import io.github.eduoneproject.eduone.business.domain.api.account.consts.AccountStatus;
import io.github.eduoneproject.eduone.business.kernel.security.model.UserPrincipal;
import io.github.eduoneproject.eduone.common.consts.CommonExceptionCode;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import io.github.eduoneproject.eduone.query.contract.UserAccountQueryApi;
import io.github.eduoneproject.eduone.query.contract.view.AccountAuthView;
import io.github.eduoneproject.eduone.query.contract.view.AccountView;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

/**
 * 账号认证器
 *
 * @author summerain0
 */
@Component
@RequiredArgsConstructor
public class AccountAuthenticator {
    private final PasswordEncoder passwordEncoder;
    private final UserAccountQueryApi userAccountQueryApi;

    /**
     * 认证
     *
     * @param account     账号
     * @param rawPassword 密码
     * @return 认证信息
     * @throws AuthenticationException 认证异常
     */
    public Authentication authenticate(
            String account,
            String rawPassword
    ) throws AuthenticationException {
        if (StringUtils.isBlank(account) || StringUtils.isBlank(rawPassword)) {
            throw new BasicRuntimeException(CommonExceptionCode.PARAM_REQUIRED.getCode(), "账号或密码为空");
        }

        // 验证账号密码
        AccountAuthView accountAuthView = userAccountQueryApi.getUniqueAccountAuthInfo(account);
        if (accountAuthView == null) {
            throw new BasicRuntimeException(BusinessExceptionCode.ACCOUNT_NOT_FOUND.getCode(), BusinessExceptionCode.ACCOUNT_NOT_FOUND.getDescription());
        }

        // 验证密码
        boolean passwordMatch = passwordEncoder.matches(rawPassword, accountAuthView.getAuthSecret());
        if (!passwordMatch) {
            throw new BasicRuntimeException(BusinessExceptionCode.ACCOUNT_PASSWORD_INVALID.getCode(), BusinessExceptionCode.ACCOUNT_PASSWORD_INVALID.getDescription());
        }
        // 检查账号认证状态
        if (Objects.equals(accountAuthView.getStatus(), AccountAuthStatus.INACTIVE.getCode())) {
            throw new BasicRuntimeException(BusinessExceptionCode.ACCOUNT_AUTH_INACTIVE.getCode(), BusinessExceptionCode.ACCOUNT_AUTH_INACTIVE.getDescription());
        }

        AccountView accountInfoView = userAccountQueryApi.getAccountInfoById(accountAuthView.getAccountId());
        if (accountInfoView == null) {
            throw new BasicRuntimeException(BusinessExceptionCode.ACCOUNT_NOT_FOUND.getCode(), BusinessExceptionCode.ACCOUNT_NOT_FOUND.getDescription());
        }
        // 检查账户状态
        if (Objects.equals(accountInfoView.getStatus(), AccountStatus.INACTIVE.getCode())) {
            throw new BasicRuntimeException(BusinessExceptionCode.ACCOUNT_INACTIVE.getCode(), BusinessExceptionCode.ACCOUNT_INACTIVE.getDescription());
        }

        UserPrincipal principal = UserPrincipal.of(accountInfoView.getUserId());
        return new PrincipalAuthenticationToken(principal, Collections.emptyList());
    }
}