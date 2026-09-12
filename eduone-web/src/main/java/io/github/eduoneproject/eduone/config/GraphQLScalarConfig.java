package io.github.eduoneproject.eduone.config;

import graphql.language.FieldDefinition;
import graphql.language.ListType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.TypeName;
import graphql.scalars.ExtendedScalars;
import graphql.schema.idl.SchemaGenerator;
import io.github.eduoneproject.eduone.config.graphql.CommonBusinessResponseDirectiveScanner;
import io.github.eduoneproject.eduone.config.graphql.PageDirectiveScanner;
import org.springframework.boot.graphql.autoconfigure.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.util.Set;

/**
 * graphql自定义类型配置
 *
 * @author summerain0
 */
@Configuration
public class GraphQLScalarConfig {
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.GraphQLLong);
    }

    @Bean
    public GraphQlSourceBuilderCustomizer graphQlSourceBuilderCustomizer() {
        PageDirectiveScanner pageDirectiveScanner = new PageDirectiveScanner();
        Set<String> pageTargetTypeSet = pageDirectiveScanner.scanPageDirectives();
        CommonBusinessResponseDirectiveScanner commonBusinessResponseDirectiveScanner = new CommonBusinessResponseDirectiveScanner();
        Set<String> commonBusinessResponseTargetTypeSet = commonBusinessResponseDirectiveScanner.scanDirectives();
        return builder -> builder.schemaFactory((typeDefinitionRegistry, runtimeWiring) -> {
            // 分页
            for (String targetType : pageTargetTypeSet) {
                String pageResultType = targetType + "DataPage";

                ObjectTypeDefinition pageResultTypeDef = ObjectTypeDefinition.newObjectTypeDefinition()
                        .name(pageResultType)
                        .fieldDefinition(FieldDefinition.newFieldDefinition()
                                .name("currentPageData")
                                .type(new ListType(new TypeName(targetType)))
                                .build())
                        .fieldDefinition(FieldDefinition.newFieldDefinition()
                                .name("totalSize")
                                .type(new TypeName("Long"))
                                .build())
                        .fieldDefinition(FieldDefinition.newFieldDefinition()
                                .name("pageNo")
                                .type(new TypeName("Long"))
                                .build())
                        .fieldDefinition(FieldDefinition.newFieldDefinition()
                                .name("pageSize")
                                .type(new TypeName("Long"))
                                .build())
                        .build();

                typeDefinitionRegistry.add(pageResultTypeDef);
            }

            // 通用业务响应
            for (String targetType : commonBusinessResponseTargetTypeSet) {
                String businessResponseResultType = targetType + "BusinessResponse";

                ObjectTypeDefinition commonBusinessResponseResultTypeDef = ObjectTypeDefinition.newObjectTypeDefinition()
                        .name(businessResponseResultType)
                        .fieldDefinition(FieldDefinition.newFieldDefinition()
                                .name("data")
                                .type(new TypeName(targetType))
                                .build())
                        .fieldDefinition(FieldDefinition.newFieldDefinition()
                                .name("code")
                                .type(new TypeName("String"))
                                .build())
                        .fieldDefinition(FieldDefinition.newFieldDefinition()
                                .name("message")
                                .type(new TypeName("String"))
                                .build())
                        .build();

                typeDefinitionRegistry.add(commonBusinessResponseResultTypeDef);
            }
            SchemaGenerator schemaGenerator = new SchemaGenerator();
            return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        });
    }
}