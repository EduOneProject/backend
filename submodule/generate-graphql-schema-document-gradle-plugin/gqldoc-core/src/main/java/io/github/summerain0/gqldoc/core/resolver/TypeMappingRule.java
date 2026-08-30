package io.github.summerain0.gqldoc.core.resolver;

import io.github.summerain0.gqldoc.core.enums.TypeKind;
import io.github.summerain0.gqldoc.core.ir.TypeDef;
import io.github.summerain0.gqldoc.core.ir.TypeRef;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * 类型映射规则
 *
 * @author summerain0
 */
@AllArgsConstructor
public class TypeMappingRule {
    /**
     * 全限定名称通配符
     */
    public static final String QUALIFIED_NAME_WILDCARD = "[Wildcard-QualifiedName]";

    /**
     * 全限定名称
     */
    @Getter
    private final String qualifiedName;

    /**
     * 优先级
     */
    @Getter
    private final int priority;

    /**
     * 类型转换函数
     */
    private final BiFunction<String, TypeDefContext, TypeDef> transformDef;

    /**
     * 引用转换函数
     */
    private final BiFunction<String, List<TypeRef>, TypeRef> transformRef;

    /**
     * 简单的类型映射规则
     *
     * @param javaFqName  java全限定名称
     * @param graphqlName graphql名称
     * @param kind        类型Kind
     * @param priority    优先级
     * @return 类型映射规则
     */
    public static TypeMappingRule simpleRule(
            String javaFqName,
            String graphqlName,
            TypeKind kind,
            int priority
    ) {
        return new TypeMappingRule(
                javaFqName,
                priority,
                (name, context) -> TypeDef.builder()
                        .name(graphqlName)
                        .kind(kind)
                        .description(context.getDescription())
                        .build(),
                (name, args) -> TypeRef.builder().name(graphqlName).kind(kind).build()
        );
    }

    /**
     * 是否匹配
     *
     * @param targetQualifiedName 全限定名称
     * @return 是否匹配
     */
    public boolean matches(String targetQualifiedName) {
        return qualifiedName.equals(targetQualifiedName) || qualifiedName.equals(QUALIFIED_NAME_WILDCARD);
    }

    /**
     * 转换定义
     *
     * @param qualifiedName 全限定名称
     * @param context       类型定义上下文
     * @return 转换后的定义
     */
    public TypeDef transformDef(String qualifiedName, TypeDefContext context) {
        return transformDef.apply(qualifiedName, context);
    }

    /**
     * 转换引用
     *
     * @param qualifiedName 全限定名称
     * @param genericArgs   泛型参数
     * @return 转换后的引用
     */
    public TypeRef transformRef(String qualifiedName, List<TypeRef> genericArgs) {
        return transformRef.apply(qualifiedName, genericArgs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeMappingRule that)) return false;
        return qualifiedName.equals(that.qualifiedName) && priority == that.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, priority);
    }
}
