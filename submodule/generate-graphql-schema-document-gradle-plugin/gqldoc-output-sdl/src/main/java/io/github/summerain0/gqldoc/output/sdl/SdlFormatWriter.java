package io.github.summerain0.gqldoc.output.sdl;

import io.github.summerain0.gqldoc.core.enums.TypeKind;
import io.github.summerain0.gqldoc.core.ir.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SDL格式化输出
 *
 * @author summerain0
 */
public class SdlFormatWriter {
    private static final String INDENT = "    ";

    /**
     * 输出SDL文件
     *
     * @param schemaIRList schema中间模型列表
     * @param outputDir    输出目录
     */
    public static void write(List<SchemaIR> schemaIRList, Path outputDir) {
        try {
            System.out.printf("开始输出SDL文件到目录：%s，schema数量：%d%n", outputDir, schemaIRList.size());

            // 合并类型定义
            System.out.println("开始合并类型定义");
            Map<Pair<String, TypeKind>, TypeDef> typeDefMap = new HashMap<>();

            List<TypeDef> typeDefs = schemaIRList.stream()
                    .flatMap(schemaIR -> schemaIR.getDefinitions().values().stream())
                    .toList();
            for (TypeDef typeDef : typeDefs) {
                typeDefMap.put(Pair.of(typeDef.getName(), typeDef.getKind()), typeDef);
            }

            List<String> typeDefNameList = typeDefMap.values().stream().map(TypeDef::getName).toList();
            Set<String> duplicates = typeDefNameList.stream()
                    .filter(e -> Collections.frequency(typeDefNameList, e) > 1)
                    .collect(Collectors.toSet());
            if (!duplicates.isEmpty()) {
                throw new RuntimeException("类型定义名称重复：" + duplicates);
            }
            typeDefs = typeDefMap.values().stream().toList();

            System.out.println("开始输出类型定义文件");
            boolean hasQueryField = schemaIRList.stream().anyMatch(schemaIR -> schemaIR.getQueryFields() != null && !schemaIR.getQueryFields().isEmpty());
            boolean hasMutationField = schemaIRList.stream().anyMatch(schemaIR -> schemaIR.getMutationFields() != null && !schemaIR.getMutationFields().isEmpty());
            String typeDefinitionFileContent = buildTypeDefinitionFileContent(typeDefs, hasQueryField, hasMutationField);
            Files.writeString(Paths.get(outputDir.toString(), "schema.graphql"), typeDefinitionFileContent);

            System.out.println("开始输出Schema文件");
            for (SchemaIR schemaIR : schemaIRList) {
                String fileName = getSchemaSdlFileName(schemaIR);
                String schemaFileContent = buildSchemaFileContent(schemaIR);
                Files.writeString(Paths.get(outputDir.toString(), fileName), schemaFileContent);
            }

            System.out.printf("结束输出SDL文件到目录：%s%n", outputDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取schema的sdl文件名
     *
     * @param schemaIR schema中间模型
     * @return sdl文件名
     */
    private static String getSchemaSdlFileName(SchemaIR schemaIR) {
        return schemaIR.getSchemaName() + ".graphql";
    }

    /**
     * 构建类型定义文件内容
     *
     * @param typeDefList      类型定义列表
     * @param hasQueryField    是否有查询字段
     * @param hasMutationField 是否有变更字段
     * @return 文件内容
     */
    private static String buildTypeDefinitionFileContent(List<TypeDef> typeDefList, boolean hasQueryField, boolean hasMutationField) {
        StringBuilder content = new StringBuilder();
        // 基础定义
        if (hasQueryField) {
            content.append("type Query\n");
        }
        if (hasMutationField) {
            content.append("type Mutation\n");
        }
        content.append("directive @page(for: String!) on FIELD_DEFINITION\n");
        content.append("directive @commonBusinessResponse(for: String!) on FIELD_DEFINITION\n");

        // 解析类型
        if (CollectionUtils.isEmpty(typeDefList)) {
            return content.toString();
        }
        typeDefList = typeDefList.stream().sorted(Comparator.comparing(TypeDef::getName)).toList();

        List<String> scalarDescList = new ArrayList<>();
        List<String> objectDescList = new ArrayList<>();
        List<String> enumDescList = new ArrayList<>();
        List<String> inputObjectDescList = new ArrayList<>();

        for (TypeDef typeDef : typeDefList) {
            TypeKind kind = typeDef.getKind();
            if (kind == TypeKind.SCALAR) {
                String defContent = "scalar " + typeDef.getName();
                scalarDescList.add(defContent);
            } else if (kind == TypeKind.OBJECT) {
                StringBuilder defContent = new StringBuilder();
                if (StringUtils.isNotBlank(typeDef.getDescription())) {
                    defContent.append("\"\"\"").append(typeDef.getDescription()).append("\"\"\"\n");
                }
                defContent.append("type ").append(typeDef.getName()).append(" {\n");
                List<FieldDef> fieldDefList = typeDef.getFields();
                if (CollectionUtils.isEmpty(fieldDefList)) {
                    throw new RuntimeException("类型定义字段为空：" + typeDef);
                }
                for (FieldDef fieldDef : fieldDefList) {
                    if (StringUtils.isNotBlank(fieldDef.getDescription())) {
                        defContent.append(INDENT).append("\"\"\"").append(fieldDef.getDescription()).append("\"\"\"\n");
                    }
                    defContent.append(INDENT).append(fieldDef.getName()).append(": ").append(fieldDef.getType()).append("\n");
                }
                defContent.append("}");
                objectDescList.add(defContent.toString());
            } else if (kind == TypeKind.INPUT) {
                StringBuilder defContent = new StringBuilder();
                if (StringUtils.isNotBlank(typeDef.getDescription())) {
                    defContent.append("\"\"\"").append(typeDef.getDescription()).append("\"\"\"\n");
                }
                defContent.append("input ").append(typeDef.getName()).append(" {\n");
                List<FieldDef> fieldDefList = typeDef.getFields();
                if (CollectionUtils.isEmpty(fieldDefList)) {
                    throw new RuntimeException("类型定义字段为空：" + typeDef);
                }
                for (FieldDef fieldDef : fieldDefList) {
                    if (StringUtils.isNotBlank(fieldDef.getDescription())) {
                        defContent.append(INDENT).append("\"\"\"").append(fieldDef.getDescription()).append("\"\"\"\n");
                    }
                    defContent.append(INDENT).append(fieldDef.getName()).append(": ").append(fieldDef.getType()).append("\n");
                }
                defContent.append("}");
                inputObjectDescList.add(defContent.toString());
            } else if (kind == TypeKind.ENUM) {
                StringBuilder defContent = new StringBuilder();
                if (StringUtils.isNotBlank(typeDef.getDescription())) {
                    defContent.append("\"\"\"").append(typeDef.getDescription()).append("\"\"\"\n");
                }
                defContent.append("enum ").append(typeDef.getName()).append(" {\n");
                List<FieldDef> fieldDefList = typeDef.getFields();
                if (CollectionUtils.isEmpty(fieldDefList)) {
                    throw new RuntimeException("类型定义字段为空：" + typeDef);
                }
                for (FieldDef fieldDef : fieldDefList) {
                    if (StringUtils.isNotBlank(fieldDef.getDescription())) {
                        defContent.append(INDENT).append("\"\"\"").append(fieldDef.getDescription()).append("\"\"\"\n");
                    }
                    defContent.append(INDENT).append(fieldDef.getName()).append("\n");
                }
                defContent.append("}");
                enumDescList.add(defContent.toString());
            } else {
                throw new RuntimeException("不支持的类型定义：" + typeDef);
            }
        }

        List<String> typeDefContent = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(scalarDescList)) { // 标量
            typeDefContent.add(String.join("\n", scalarDescList));
        }
        if (CollectionUtils.isNotEmpty(objectDescList)) { // 类型
            typeDefContent.add(String.join("\n\n", objectDescList));
        }
        if (CollectionUtils.isNotEmpty(inputObjectDescList)) { // 输入对象
            typeDefContent.add(String.join("\n\n", inputObjectDescList));
        }
        if (CollectionUtils.isNotEmpty(enumDescList)) { // 枚举
            typeDefContent.add(String.join("\n\n", enumDescList));
        }
        content.append(String.join("\n\n", typeDefContent));

        return content.toString();
    }

    /**
     * 构建Schema文件内容
     *
     * @param schemaIR Schema信息
     * @return 文件内容
     */
    private static String buildSchemaFileContent(SchemaIR schemaIR) {
        StringBuilder content = new StringBuilder();
        if (CollectionUtils.isNotEmpty(schemaIR.getMutationFields())) {
            List<OperationField> mutationFieldList = schemaIR.getMutationFields();
            mutationFieldList = mutationFieldList.stream().sorted(Comparator.comparing(OperationField::getName)).toList();
            content.append("extend type Mutation {\n");

            for (OperationField operationField : mutationFieldList) {
                // 注释
                String description = operationField.getDescription();
                if (StringUtils.isNotBlank(description)) {
                    content.append(INDENT).append("\"\"\"").append(description).append("\"\"\"\n");
                }
                // 名称
                content.append(INDENT).append(operationField.getName());
                // 参数
                if (CollectionUtils.isNotEmpty(operationField.getArguments())) {
                    List<ArgumentDef> argumentDefList = operationField.getArguments();
                    String argument = argumentDefList.stream().map(ArgumentDef::toString).collect(Collectors.joining(", "));
                    content.append("(").append(argument).append(")");
                }
                // 返回值
                content.append(": ").append(operationField.getReturnType());
                content.append("\n");
            }

            content.append("}");
        }

        if (CollectionUtils.isNotEmpty(schemaIR.getQueryFields())) {
            List<OperationField> queryFieldList = schemaIR.getQueryFields();
            queryFieldList = queryFieldList.stream().sorted(Comparator.comparing(OperationField::getName)).toList();
            if (CollectionUtils.isNotEmpty(schemaIR.getMutationFields())) {
                content.append("\n\n");
            }
            content.append("extend type Query {\n");

            for (OperationField operationField : queryFieldList) {
                // 注释
                String description = operationField.getDescription();
                if (StringUtils.isNotBlank(description)) {
                    content.append(INDENT).append("\"\"\"").append(description).append("\"\"\"\n");
                }
                // 名称
                content.append(INDENT).append(operationField.getName());
                // 参数
                if (CollectionUtils.isNotEmpty(operationField.getArguments())) {
                    List<ArgumentDef> argumentDefList = operationField.getArguments();
                    String argument = argumentDefList.stream().map(ArgumentDef::toString).collect(Collectors.joining(", "));
                    content.append("(").append(argument).append(")");
                }
                // 返回值
                content.append(": ").append(operationField.getReturnType());
                content.append("\n");
            }

            content.append("}");
        }
        return content.toString();
    }
}
