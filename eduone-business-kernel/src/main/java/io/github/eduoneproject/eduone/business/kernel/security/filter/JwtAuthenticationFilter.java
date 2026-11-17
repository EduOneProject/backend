package io.github.eduoneproject.eduone.business.kernel.security.filter;

import io.github.eduoneproject.eduone.business.kernel.security.auth.PrincipalAuthenticationToken;
import io.github.eduoneproject.eduone.business.kernel.security.model.UserPrincipal;
import io.github.eduoneproject.eduone.business.kernel.service.TokenService;
import io.github.eduoneproject.eduone.business.kernel.service.model.AccessTokenJwtPayload;
import io.github.eduoneproject.eduone.common.util.SpringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 *
 * @author summerain0
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            TokenService tokenService = SpringUtils.getBean(TokenService.class);
            AccessTokenJwtPayload accessTokenJwtPayload = tokenService.extractToken(request);
            if (accessTokenJwtPayload != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserPrincipal principal = UserPrincipal.of(accessTokenJwtPayload.getUserId(), accessTokenJwtPayload.getAccountId());
                PrincipalAuthenticationToken authentication = new PrincipalAuthenticationToken(principal, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT认证失败", e);
            SecurityContextHolder.clearContext();
            AuthenticationEntryPoint authenticationEntryPoint = SpringUtils.getBean(AuthenticationEntryPoint.class);
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new InsufficientAuthenticationException(e.getMessage(), e)
            );
        }
    }
}