package io.github.summerain0.gqldoc.core.spi;

import io.github.summerain0.gqldoc.core.resolver.TypeMappingRule;

import java.util.List;

/**
 * 类型映射规则提供者
 *
 * @author summerain0
 */
public interface TypeMappingProvider {
    /**
     * 提供者名称
     */
    String providerName();

    /**
     * 提供类型映射规则
     */
    List<TypeMappingRule> provideRules();
}