package io.github.eduoneproject.eduone.business.kernel.config.filter;

import io.github.eduoneproject.eduone.business.kernel.service.TokenService;
import io.github.eduoneproject.eduone.business.kernel.service.model.AccessTokenJwtPayload;
import io.github.eduoneproject.eduone.common.domain.context.HttpIdentity;
import io.github.eduoneproject.eduone.common.domain.context.ServiceContext;
import io.github.eduoneproject.eduone.common.domain.context.ServiceContextLocal;
import io.github.eduoneproject.eduone.common.domain.context.UserIdentity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.waterfallio.ip4j.Ip4j;

import java.io.IOException;

/**
 * 服务上下文过滤器
 *
 * @author summerain0
 */
@RequiredArgsConstructor
@Component
public class ServiceContextFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        ServiceContext serviceContext = new ServiceContext();
        // 用户标识信息
        AccessTokenJwtPayload accessTokenJwtPayload = tokenService.extractToken(request);
        if (accessTokenJwtPayload != null) {
            UserIdentity.UserIdentityBuilder builder = UserIdentity.builder();
            builder.sessionId(accessTokenJwtPayload.getSessionId());
            builder.userId(accessTokenJwtPayload.getUserId());
            builder.accountId(accessTokenJwtPayload.getAccountId());
            serviceContext.setUserIdentity(builder.build());
        }
        // http标识信息
        HttpIdentity.HttpIdentityBuilder httpIdentityBuilder = HttpIdentity.builder();
        httpIdentityBuilder.ipAddress(Ip4j.getIp(request));
        httpIdentityBuilder.domain(request.getServerName());
        httpIdentityBuilder.requestUrl(request.getRequestURI());
        httpIdentityBuilder.userAgent(request.getHeader("User-Agent"));
        serviceContext.setHttpIdentity(httpIdentityBuilder.build());
        ServiceContextLocal.setServiceContext(serviceContext);
        try {
            filterChain.doFilter(request, response);
        } finally {
            ServiceContextLocal.removeServiceContext();
        }
    }
}
