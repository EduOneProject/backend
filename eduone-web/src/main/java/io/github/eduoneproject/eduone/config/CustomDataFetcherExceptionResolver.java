package io.github.eduoneproject.eduone.config;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import io.github.eduoneproject.eduone.consts.ApplicationErrorType;
import io.github.eduoneproject.eduone.common.consts.CommonExceptionCode;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义DataFetcher异常处理器
 *
 * @author summerain0
 */
@Slf4j
@Component
public class CustomDataFetcherExceptionResolver implements DataFetcherExceptionResolver {
    public static final String EXTENSION_KEY_CODE = "code";
    public static final String EXTENSION_KEY_RETRYABLE = "retryable";

    @NullMarked
    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment environment) {
        if (exception instanceof IllegalArgumentException) { // 参数异常
            GraphQLError error = GraphqlErrorBuilder.newError()
                    .errorType(ApplicationErrorType.BAD_REQUEST)
                    .message(exception.getMessage())
                    .path(environment.getExecutionStepInfo().getPath())
                    .location(environment.getField().getSourceLocation())
                    .extensions(buildExtensions(exception))
                    .build();
            return Mono.just(Collections.singletonList(error));
        } else if (exception instanceof ConstraintViolationException constraintViolationException) { // 参数异常
            Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
            GraphQLError error = GraphqlErrorBuilder.newError()
                    .errorType(ApplicationErrorType.BAD_REQUEST)
                    .message(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";")))
                    .path(environment.getExecutionStepInfo().getPath())
                    .location(environment.getField().getSourceLocation())
                    .extensions(buildExtensions(exception))
                    .build();
            return Mono.just(Collections.singletonList(error));
        } else if (exception instanceof BasicRuntimeException basicRuntimeException) { // 业务异常
            ApplicationErrorType applicationErrorType = getBusinessErrorType(basicRuntimeException);
            if (applicationErrorType == ApplicationErrorType.INTERNAL_ERROR) {
                log.error("系统发生异常", exception);
            } else {
                log.warn("业务发生异常", exception);
            }
            GraphQLError error = GraphqlErrorBuilder.newError()
                    .errorType(applicationErrorType)
                    .message(basicRuntimeException.getMessage())
                    .path(environment.getExecutionStepInfo().getPath())
                    .location(environment.getField().getSourceLocation())
                    .extensions(buildExtensions(exception))
                    .build();
            return Mono.just(Collections.singletonList(error));
        }
        log.error("系统发生异常", exception);
        // 默认处理
        GraphQLError error = GraphqlErrorBuilder.newError()
                .errorType(ApplicationErrorType.INTERNAL_ERROR)
                .message("系统异常，请联系平台管理员")
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .extensions(buildExtensions(exception))
                .build();
        return Mono.just(Collections.singletonList(error));
    }

    /**
     * 获取业务异常类型
     *
     * @param exception 异常
     * @return 业务异常类型
     */
    private ApplicationErrorType getBusinessErrorType(BasicRuntimeException exception) {
        return Optional.ofNullable(exception)
                .map(BasicRuntimeException::getCode)
                .map(code -> code / 1_000_000)
                .map(ApplicationErrorType::valueOf)
                .orElse(ApplicationErrorType.INTERNAL_ERROR);
    }

    /**
     * 构建异常扩展信息
     *
     * @param exception 异常
     * @return 异常扩展信息
     */
    private Map<String, Object> buildExtensions(Throwable exception) {
        if (exception == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> extensions = new HashMap<>();
        if (exception instanceof BasicRuntimeException basicRuntimeException) { // 业务异常
            ApplicationErrorType applicationErrorType = getBusinessErrorType(basicRuntimeException);
            extensions.put(EXTENSION_KEY_CODE, basicRuntimeException.getCode());
            extensions.put(EXTENSION_KEY_RETRYABLE, ApplicationErrorType.retryable(applicationErrorType));
        } else { // 其他异常
            extensions.put(EXTENSION_KEY_CODE, CommonExceptionCode.INTERNAL_ERROR.getCode());
            extensions.put(EXTENSION_KEY_RETRYABLE, true);
        }
        return extensions;
    }
}