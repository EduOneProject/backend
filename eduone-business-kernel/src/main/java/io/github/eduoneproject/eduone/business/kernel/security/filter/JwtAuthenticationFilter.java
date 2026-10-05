package io.github.eduoneproject.eduone.business.kernel.security.filter;

import io.github.eduoneproject.eduone.business.kernel.security.auth.PrincipalAuthenticationToken;
import io.github.eduoneproject.eduone.business.kernel.security.model.UserPrincipal;
import io.github.eduoneproject.eduone.business.kernel.service.TokenService;
import io.github.eduoneproject.eduone.common.util.SpringUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (StringUtils.isNotBlank(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                TokenService tokenService = SpringUtils.getBean(TokenService.class);
                Claims claims = tokenService.parseAccessToken(token);
                UserPrincipal principal = UserPrincipal.of(claims.getSubject());
                PrincipalAuthenticationToken authentication = new PrincipalAuthenticationToken(principal, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("JWT认证失败", e);
            SecurityContextHolder.clearContext();
            AuthenticationEntryPoint authenticationEntryPoint = SpringUtils.getBean(AuthenticationEntryPoint.class);
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new InsufficientAuthenticationException(e.getMessage(), e)
            );
        }
    }

    /**
     * 从请求头中提取JWT令牌
     *
     * @param request HTTP请求对象
     * @return JWT令牌字符串，如果不存在则返回null
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}