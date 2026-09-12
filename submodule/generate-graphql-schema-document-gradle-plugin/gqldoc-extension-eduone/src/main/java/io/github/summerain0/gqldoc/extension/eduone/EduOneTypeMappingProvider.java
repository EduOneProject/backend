package io.github.summerain0.gqldoc.extension.eduone;

import io.github.summerain0.gqldoc.core.ir.DirectiveRef;
import io.github.summerain0.gqldoc.core.ir.TypeRef;
import io.github.summerain0.gqldoc.core.resolver.TypeMappingRule;
import io.github.summerain0.gqldoc.core.spi.TypeMappingProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EduOne项目差异化类型转换规则提供者
 *
 * @author summerain0
 */
public class EduOneTypeMappingProvider implements TypeMappingProvider {
    @Override
    public String providerName() {
        return "EduOne";
    }

    @Override
    public List<TypeMappingRule> provideRules() {
        return List.of(
                new TypeMappingRule(
                        "io.github.eduoneproject.eduone.common.domain.DataPage",
                        Integer.MAX_VALUE,
                        (qualifiedName, context) -> null, // 通过指令动态生成
                        (qualifiedName, genericArgs) -> {
                            if (genericArgs.size() != 1) {
                                throw new IllegalArgumentException("DataPage应当有且只有一个泛型");
                            }
                            TypeRef typeRef = genericArgs.get(0);
                            // 名称
                            String typeRefName = typeRef.getName();
                            String simpleName = typeRefName + "DataPage";
                            // 指令
                            List<DirectiveRef> directives = new ArrayList<>();
                            DirectiveRef directiveRef = DirectiveRef.builder()
                                    .name("page")
                                    .arguments(Map.of("for", typeRefName))
                                    .build();
                            directives.add(directiveRef);
                            return TypeRef.builder()
                                    .name(simpleName)
                                    .directives(directives)
                                    .build();
                        }
                ),
                new TypeMappingRule(
                        "io.github.eduoneproject.eduone.business.gateway.response.CommonBusinessResponse",
                        Integer.MAX_VALUE,
                        (qualifiedName, context) -> null, // 通过指令动态生成
                        (qualifiedName, genericArgs) -> {
                            if (genericArgs.size() != 1) {
                                throw new IllegalArgumentException("CommonBusinessResponse应当有且只有一个泛型");
                            }
                            TypeRef typeRef = genericArgs.get(0);
                            // 名称
                            String typeRefName = typeRef.getName();
                            String simpleName = typeRefName + "BusinessResponse";
                            // 指令
                            List<DirectiveRef> directives = new ArrayList<>();
                            DirectiveRef directiveRef = DirectiveRef.builder()
                                    .name("commonBusinessResponse")
                                    .arguments(Map.of("for", typeRefName))
                                    .build();
                            directives.add(directiveRef);
                            return TypeRef.builder()
                                    .name(simpleName)
                                    .directives(directives)
                                    .build();
                        }
                )
        );
    }
}
