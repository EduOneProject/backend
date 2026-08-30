package io.github.summerain0.gqldoc.core.extension;

import io.github.summerain0.gqldoc.core.ir.TypeDef;
import io.github.summerain0.gqldoc.core.ir.TypeRef;
import io.github.summerain0.gqldoc.core.resolver.TypeDefContext;
import io.github.summerain0.gqldoc.core.resolver.TypeMappingRule;
import io.github.summerain0.gqldoc.core.spi.TypeMappingProvider;

import java.util.*;

/**
 * 扩展注册器
 *
 * @author summerain0
 */
public class ExtensionRegistry {
    private static volatile ExtensionRegistry INSTANCE;

    /**
     * 类型映射规则
     */
    private final List<TypeMappingRule> rules = new ArrayList<>();

    /**
     * 注册类型映射规则
     *
     * @param rule 规则
     */
    public void register(TypeMappingRule rule) {
        if (rule == null) {
            return;
        }
        rules.add(rule);
        rules.sort(Comparator.comparingInt(TypeMappingRule::getPriority).reversed());
    }

    /**
     * 注册类型映射规则
     *
     * @param typeMappingProvider 规则提供者
     */
    public void register(TypeMappingProvider typeMappingProvider) {
        if (typeMappingProvider == null) {
            return;
        }
        registerAll(typeMappingProvider.provideRules());
    }

    /**
     * 注册类型映射规则
     *
     * @param newRules 规则
     */
    public void registerAll(Collection<TypeMappingRule> newRules) {
        if (newRules == null) {
            return;
        }
        newRules.forEach(this::register);
    }

    /**
     * 转换类型
     *
     * @param qualifiedName 类型全限定名
     * @return 转换后的类型
     */
    public TypeDef transformDef(String qualifiedName, TypeDefContext context) {
        TypeDef typeDef = rules.stream()
                .filter(rule -> rule.matches(qualifiedName))
                .findFirst()
                .map(rule -> rule.transformDef(qualifiedName, context))
                .orElse(null);
        if (typeDef == null) {
            throw new RuntimeException("不受支持的类型转换：" + qualifiedName);
        }
        return typeDef;
    }

    /**
     * 转换引用
     *
     * @param qualifiedName 类型全限定名
     * @return 转换后的引用
     */
    public TypeRef transformRef(String qualifiedName, List<TypeRef> genericArgs) {
        TypeRef typeRef = rules.stream()
                .filter(rule -> rule.matches(qualifiedName))
                .findFirst()
                .map(rule -> rule.transformRef(qualifiedName, genericArgs))
                .orElse(null);
        if (typeRef == null) {
            throw new RuntimeException("不受支持的类型转换：" + qualifiedName);
        }
        return typeRef;
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static ExtensionRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (ExtensionRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ExtensionRegistry();
                    // 注册所有提供者
                    ServiceLoader<TypeMappingProvider> typeMappingProviders = ServiceLoader.load(TypeMappingProvider.class);
                    typeMappingProviders.forEach(INSTANCE::register);
                }
            }
        }
        return INSTANCE;
    }
}
