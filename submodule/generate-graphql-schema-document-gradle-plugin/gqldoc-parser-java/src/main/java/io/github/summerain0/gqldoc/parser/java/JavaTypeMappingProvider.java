package io.github.summerain0.gqldoc.parser.java;

import io.github.summerain0.gqldoc.core.enums.TypeKind;
import io.github.summerain0.gqldoc.core.ir.TypeDef;
import io.github.summerain0.gqldoc.core.ir.TypeRef;
import io.github.summerain0.gqldoc.core.resolver.TypeMappingRule;
import io.github.summerain0.gqldoc.core.spi.TypeMappingProvider;
import io.github.summerain0.gqldoc.parser.java.util.ClassUtils;

import java.util.List;

/**
 * Java类型转换规则提供者
 *
 * @author summerain0
 */
public class JavaTypeMappingProvider implements TypeMappingProvider {
    /**
     * 全局优先级
     */
    private final int GLOBAL_PRIORITY = 100;

    @Override
    public String providerName() {
        return "JDK";
    }

    @Override
    public List<TypeMappingRule> provideRules() {
        return List.of(
                TypeMappingRule.simpleRule("long", "Long", TypeKind.SCALAR, GLOBAL_PRIORITY),
                TypeMappingRule.simpleRule("java.lang.Long", "Long", TypeKind.SCALAR, GLOBAL_PRIORITY),
                TypeMappingRule.simpleRule("int", "Int", TypeKind.SCALAR, GLOBAL_PRIORITY),
                TypeMappingRule.simpleRule("java.lang.Integer", "Int", TypeKind.SCALAR, GLOBAL_PRIORITY),
                TypeMappingRule.simpleRule("java.time.OffsetDateTime", "DateTime", TypeKind.SCALAR, GLOBAL_PRIORITY),
                TypeMappingRule.simpleRule("java.util.Date", "DateTime", TypeKind.SCALAR, GLOBAL_PRIORITY),
                TypeMappingRule.simpleRule("java.lang.String", "String", TypeKind.SCALAR, GLOBAL_PRIORITY),
                new TypeMappingRule(
                        "java.util.Collection",
                        GLOBAL_PRIORITY,
                        (qualifiedName, context) -> null, // 集合类型无需定义
                        (qualifiedName, genericArgs) -> {
                            if (genericArgs.size() != 1) {
                                throw new IllegalArgumentException("集合应当有且只有一个泛型");
                            }
                            TypeRef typeRef = genericArgs.get(0);
                            String typeRefName = typeRef.getName();
                            return TypeRef.builder()
                                    .name("[" + typeRefName + "]")
                                    .kind(TypeKind.LIST)
                                    .ofType(typeRef)
                                    .build();
                        }
                ),
                new TypeMappingRule(
                        TypeMappingRule.QUALIFIED_NAME_WILDCARD,
                        Integer.MIN_VALUE,
                        (qualifiedName, context) -> {
                            String simpleName = ClassUtils.getSimpleName(qualifiedName);
                            return TypeDef.builder()
                                    .name(simpleName)
                                    .description(context.getDescription())
                                    .kind(context.getTypeKind())
                                    .fields(context.getFieldDefList())
                                    .build();
                        },
                        (qualifiedName, genericArgs) -> {
                            String simpleName = ClassUtils.getSimpleName(qualifiedName);
                            return TypeRef.builder()
                                    .name(simpleName)
                                    .build();
                        }
                )
        );
    }
}
