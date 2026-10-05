package io.github.eduoneproject.eduone.business.gateway.config;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import io.github.eduoneproject.eduone.common.consts.CommonExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GraphQL 认证异常入口点
 *
 * @author summerain0
 */
@Component
@RequiredArgsConstructor
public class GraphQlAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull AuthenticationException authException
    ) throws IOException {
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("code", CommonExceptionCode.UNAUTHORIZED.getCode());
        extensions.put("retryable", false);

        GraphQLError error = GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(CommonExceptionCode.UNAUTHORIZED.getDescription())
                .extensions(extensions)
                .build();

        Map<String, Object> errorMap = error.toSpecification();
        Map<String, Object> body = Map.of("errors", List.of(errorMap));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), body);
    }
}