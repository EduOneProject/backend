package io.github.summerain0.gqldoc.parser.java;

import io.github.summerain0.gqldoc.core.enums.TypeKind;
import io.github.summerain0.gqldoc.core.ir.ArgumentDef;
import io.github.summerain0.gqldoc.core.ir.DirectiveDef;
import io.github.summerain0.gqldoc.core.ir.DirectiveRef;
import io.github.summerain0.gqldoc.core.ir.TypeDef;
import io.github.summerain0.gqldoc.core.ir.TypeRef;
import io.github.summerain0.gqldoc.core.resolver.TypeMappingRule;
import io.github.summerain0.gqldoc.core.spi.TypeMappingProvider;
import io.github.summerain0.gqldoc.parser.java.util.ClassUtils;

import java.util.List;
import java.util.Map;

/**
 * Java类型转换规则提供者
 *
 * @author summerain0
 */
public class JavaTypeMappingProvider implements TypeMappingProvider {
    /**
     * 类型指令名称
     */
    private static final String TYPE_DIRECTIVE_NAME = "type";

    /**
     * 类型指令参数名称
     */
    private static final String TYPE_DIRECTIVE_ARGUMENT_NAME = "value";

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
                TypeMappingRule.simpleRule("java.lang.Boolean", "Boolean", TypeKind.SCALAR, GLOBAL_PRIORITY),
                TypeMappingRule.simpleRule("boolean", "Boolean", TypeKind.SCALAR, GLOBAL_PRIORITY),
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
                            DirectiveRef typeDirectiveRef = DirectiveRef.builder()
                                    .name(TYPE_DIRECTIVE_NAME)
                                    .arguments(Map.of(TYPE_DIRECTIVE_ARGUMENT_NAME, qualifiedName))
                                    .build();
                            return TypeDef.builder()
                                    .name(simpleName)
                                    .description(context.getDescription())
                                    .kind(context.getTypeKind())
                                    .fields(context.getFieldDefList())
                                    .directives(List.of(typeDirectiveRef))
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

    @Override
    public List<DirectiveDef> provideDirectiveDefs() {
        TypeRef stringTypeRef = TypeRef.builder()
                .name("String")
                .kind(TypeKind.SCALAR)
                .nonNullable(true)
                .build();
        ArgumentDef valueArgumentDef = ArgumentDef.builder()
                .name(TYPE_DIRECTIVE_ARGUMENT_NAME)
                .type(stringTypeRef)
                .build();
        return List.of(
                DirectiveDef.builder()
                        .name(TYPE_DIRECTIVE_NAME)
                        .arguments(List.of(valueArgumentDef))
                        .locations(List.of("OBJECT", "INPUT_OBJECT", "ENUM"))
                        .build()
        );
    }
}
