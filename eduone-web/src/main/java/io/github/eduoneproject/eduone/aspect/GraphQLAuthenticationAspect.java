package io.github.eduoneproject.eduone.aspect;

import io.github.eduoneproject.eduone.common.annotation.OptionalLogin;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * GraphQL接口鉴权判断切面
 *
 * @author summerain0
 */
@Aspect
@Component
public class GraphQLAuthenticationAspect {
    /**
     * 鉴权判断，判断当前请求是否可以免登录请求
     *
     * @param joinPoint 当前请求的JoinPoint
     */
    @Before("@annotation(org.springframework.graphql.data.method.annotation.QueryMapping) " +
            "|| @annotation(org.springframework.graphql.data.method.annotation.MutationMapping)")
    public void requireAuthenticated(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        if (method.isAnnotationPresent(OptionalLogin.class)) {
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw BasicRuntimeException.unauthorized();
        }
    }
}