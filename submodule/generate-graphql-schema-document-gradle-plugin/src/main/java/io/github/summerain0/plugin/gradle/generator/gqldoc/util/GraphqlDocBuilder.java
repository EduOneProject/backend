package io.github.summerain0.plugin.gradle.generator.gqldoc.util;

import io.github.summerain0.plugin.gradle.generator.gqldoc.model.FieldTypeInfoModel;
import io.github.summerain0.plugin.gradle.generator.gqldoc.model.GraphqlControllerInfoModel;
import io.github.summerain0.plugin.gradle.generator.gqldoc.model.GraphqlInputTypeInfoModel;
import io.github.summerain0.plugin.gradle.generator.gqldoc.model.nested.GraphqlControllerMethodInfoModel;
import io.github.summerain0.plugin.gradle.generator.gqldoc.model.nested.GraphqlFieldInfoModel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GQL文档生成器
 *
 * @author summerain0
 */
public class GraphqlDocBuilder {
    private static final String GRAPHQL_FILE_SUFFIX = ".graphql";
    private static final String GRAPHQL_RESOLVER_QUERY_CONTENT_PREFIX = "extend type Query {\n";
    private static final String GRAPHQL_RESOLVER_QUERY_CONTENT_SUFFIX = "}";
    private static final String GRAPHQL_RESOLVER_MUTATION_CONTENT_PREFIX = "extend type Mutation {\n";
    private static final String GRAPHQL_RESOLVER_MUTATION_CONTENT_SUFFIX = "}";
    private static final String GRAPHQL_RESOLVER_CONTENT_PADDING = "    ";

    /**
     * 输出文档内容
     * <p>key: 文件名</p>
     * <p>value: 文件内容</p>
     */
    private final Map<String, String> outputFileContentMap = new HashMap<>();

    /**
     * 所有源代码信息
     */
    private final List<CompilationUnit> compilationUnitList;

    /**
     * GraphQL控制器信息
     */
    private List<GraphqlControllerInfoModel> graphqlControllerInfoModelList;

    /**
     * GraphQL输入模型信息
     */
    private final Map<String, GraphqlInputTypeInfoModel> graphqlInputTypeInfoModelMap = new HashMap<>();

    /**
     * 已处理的类型全限定名
     */
    private final Set<String> processedTypeNameSet = new HashSet<>();

    /**
     * 构造方法
     *
     * @param compilationUnitList 所有源代码信息
     */
    public GraphqlDocBuilder(List<CompilationUnit> compilationUnitList) {
        this.compilationUnitList = compilationUnitList;
    }

    /**
     * 添加GraphQL控制器信息
     *
     * @param graphqlControllerInfoModelList GraphQL控制器信息
     */
    public void addAllGraphqlControllerInfoModel(List<GraphqlControllerInfoModel> graphqlControllerInfoModelList) {
        this.graphqlControllerInfoModelList = graphqlControllerInfoModelList;
    }

    /**
     * 生成GQL文档
     */
    public void generateGqlDoc() {
        for (GraphqlControllerInfoModel graphqlControllerInfoModel : graphqlControllerInfoModelList) {
            boolean hasQueryMethod = false;
            boolean hasMutationMethod = false;

            // 查询方法
            StringBuilder queryContentBuilder = new StringBuilder();
            queryContentBuilder.append(GRAPHQL_RESOLVER_QUERY_CONTENT_PREFIX);
            List<GraphqlControllerMethodInfoModel> queryMethodInfoModelList = graphqlControllerInfoModel.getMethodInfoModelList();
            for (GraphqlControllerMethodInfoModel methodInfoModel : queryMethodInfoModelList) {
                if (!methodInfoModel.isQueryMethod()) {
                    continue;
                }
                hasQueryMethod = true;

                List<Parameter> parameterList = methodInfoModel.getParameterList();
                String parameterString = parameterList.stream()
                        .map(item -> item.getNameAsString() + ": " + getResolvedTypeModel(item.getType().resolve()))
                        .collect(Collectors.joining(", "));

                for (Parameter parameter : parameterList) {
                    resolveType(parameter.getType().resolve(), true);
                }

                MethodDeclaration methodDeclaration = methodInfoModel.getMethodDeclaration();
                String comment = getComment(methodDeclaration);
                if (comment != null && !comment.isBlank()) {
                    queryContentBuilder.append(GRAPHQL_RESOLVER_CONTENT_PADDING)
                            .append("\"\"\"")
                            .append(comment)
                            .append("\"\"\"")
                            .append("\n");
                }
                queryContentBuilder.append(GRAPHQL_RESOLVER_CONTENT_PADDING).append(methodInfoModel.getMethodName());

                // 入参
                if (!parameterString.isEmpty()) {
                    queryContentBuilder.append("(").append(parameterString).append(")");
                }

                // 返回值
                Type methodReturnType = methodInfoModel.getMethodReturnType();
                resolveType(methodReturnType.resolve(), false);
                queryContentBuilder.append(": ")
                        .append(getResolvedTypeModel(methodReturnType.resolve()))
                        .append("\n");
            }
            queryContentBuilder.append(GRAPHQL_RESOLVER_QUERY_CONTENT_SUFFIX);

            // 变更方法
            StringBuilder mutationContentBuilder = new StringBuilder();
            mutationContentBuilder.append(GRAPHQL_RESOLVER_MUTATION_CONTENT_PREFIX);
            List<GraphqlControllerMethodInfoModel> mutationMethodInfoModelList = graphqlControllerInfoModel.getMethodInfoModelList();
            for (GraphqlControllerMethodInfoModel methodInfoModel : mutationMethodInfoModelList) {
                if (methodInfoModel.isQueryMethod()) {
                    continue;
                }
                hasMutationMethod = true;

                List<Parameter> parameterList = methodInfoModel.getParameterList();
                String parameterString = parameterList.stream()
                        .map(item -> item.getNameAsString() + ": " + getResolvedTypeModel(item.getType().resolve()))
                        .collect(Collectors.joining(", "));

                for (Parameter parameter : parameterList) {
                    resolveType(parameter.getType().resolve(), true);
                }

                MethodDeclaration methodDeclaration = methodInfoModel.getMethodDeclaration();
                String comment = getComment(methodDeclaration);
                if (comment != null && !comment.isBlank()) {
                    mutationContentBuilder.append(GRAPHQL_RESOLVER_CONTENT_PADDING)
                            .append("\"\"\"")
                            .append(comment)
                            .append("\"\"\"")
                            .append("\n");
                }
                mutationContentBuilder.append(GRAPHQL_RESOLVER_CONTENT_PADDING).append(methodInfoModel.getMethodName());

                // 入参
                if (!parameterString.isEmpty()) {
                    mutationContentBuilder.append("(").append(parameterString).append(")");
                }

                // 返回值
                Type methodReturnType = methodInfoModel.getMethodReturnType();
                resolveType(methodReturnType.resolve(), false);
                mutationContentBuilder.append(": ")
                        .append(getResolvedTypeModel(methodReturnType.resolve()))
                        .append("\n");
            }
            mutationContentBuilder.append(GRAPHQL_RESOLVER_MUTATION_CONTENT_SUFFIX);

            String content = "";
            if (hasQueryMethod && hasMutationMethod) {
                content = mutationContentBuilder + "\n\n" + queryContentBuilder;
            } else if (hasQueryMethod) {
                content = queryContentBuilder.toString();
            } else if (hasMutationMethod) {
                content = mutationContentBuilder.toString();
            }
            if (!content.isEmpty()) {
                outputFileContentMap.put(graphqlControllerInfoModel.getClassName(), content);
            }
        }

        StringBuilder typeBuilder = new StringBuilder();
        for (String gqlTypeName : graphqlInputTypeInfoModelMap.keySet()) {
            GraphqlInputTypeInfoModel graphqlInputTypeInfoModel = graphqlInputTypeInfoModelMap.get(gqlTypeName);
            String gqlType = graphqlInputTypeInfoModel.isParameter() ? "input" : "type";
            String classComment = graphqlInputTypeInfoModel.getComment();
            if (classComment != null && !classComment.isBlank()) {
                typeBuilder.append("\"\"\"")
                        .append(classComment)
                        .append("\"\"\"")
                        .append("\n");
            }
            typeBuilder.append(gqlType).append(" ").append(gqlTypeName).append(" {\n");
            for (GraphqlFieldInfoModel fieldInfoModel : graphqlInputTypeInfoModel.getFieldInfoModelList()) {
                String comment = fieldInfoModel.getComment();
                if (comment != null && !comment.isBlank()) {
                    typeBuilder.append(GRAPHQL_RESOLVER_CONTENT_PADDING)
                            .append("\"\"\"")
                            .append(comment)
                            .append("\"\"\"")
                            .append("\n");
                }
                typeBuilder.append(GRAPHQL_RESOLVER_CONTENT_PADDING)
                        .append(fieldInfoModel.getParameterName())
                        .append(": ")
                        .append(fieldInfoModel.getTypeName())
                        .append("\n");
            }
            typeBuilder.append("}\n\n");
        }
        outputFileContentMap.put("types", typeBuilder.toString());
    }

    /**
     * 构建文档
     *
     * @param outputFile 输出文件位置
     */
    public void build(File outputFile) throws IOException {
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }
        for (String fileName : outputFileContentMap.keySet()) {
            String fileContent = outputFileContentMap.getOrDefault(fileName, "");
            File file = new File(outputFile, fileName + GRAPHQL_FILE_SUFFIX);
            Files.writeString(file.toPath(), fileContent);
        }
    }

    /**
     * 获取方法注释
     *
     * @param methodDeclaration 方法声明
     * @return 方法注释
     */
    private String getComment(MethodDeclaration methodDeclaration) {
        Optional<Comment> commentOptional = methodDeclaration.getComment();
        if (commentOptional.isEmpty()) {
            return null;
        }
        Comment comment = commentOptional.get();
        if (comment.isJavadocComment()) {
            JavadocComment javadocComment = comment.asJavadocComment();
            Javadoc javadoc = javadocComment.parse();
            JavadocDescription javadocDescription = javadoc.getDescription();
            return javadocDescription.toText();
        }
        return comment.asString();
    }

    /**
     * 获取首个泛型
     *
     * @param resolvedType 方法返回值类型
     * @return 首个泛型类型
     */
    private ResolvedType getFirstGeneric(ResolvedType resolvedType) {
        if (resolvedType.isReferenceType()) {
            ResolvedReferenceType referenceType = resolvedType.asReferenceType();
            List<ResolvedType> typeParametersValues = referenceType.typeParametersValues();
            if (typeParametersValues.isEmpty()) {
                throw new RuntimeException("模型必须要有泛型");
            }
            return typeParametersValues.get(0);
        }
        throw new RuntimeException("不支持的类型" + resolvedType);
    }

    /**
     * 处理类型模型
     *
     * @param resolvedType 类型
     */
    private void resolveType(ResolvedType resolvedType, boolean isParameter) {
        String resolvedTypeName = getResolvedTypeModel(resolvedType).toString();
        if (resolvedType.isReferenceType()) {
            ResolvedReferenceType resolvedReferenceType = resolvedType.asReferenceType();
            String qualifiedName = resolvedReferenceType.getQualifiedName();

            if (qualifiedName.startsWith("java.")) {
                if (qualifiedName.equals("java.util.List")) {
                    ResolvedType firstGeneric = getFirstGeneric(resolvedType);
                    resolveType(firstGeneric, isParameter);
                }
            } else if (qualifiedName.equals("io.github.eduoneproject.eduone.common.domain.DataPage")) {
                ResolvedType firstGeneric = getFirstGeneric(resolvedType);
                if (!firstGeneric.isWildcard()) {
                    resolveType(firstGeneric, isParameter);
                }
            } else if (qualifiedName.equals("io.github.eduoneproject.eduone.business.gateway.response.CommonBusinessResponse")) {
                ResolvedType firstGeneric = getFirstGeneric(resolvedType);
                if (!firstGeneric.isWildcard()) {
                    resolveType(firstGeneric, isParameter);
                }
            } else if (qualifiedName.equals("io.github.eduoneproject.eduone.common.domain.DataScopeRequest")) {
                ResolvedType firstGeneric = getFirstGeneric(resolvedType);
                if (!firstGeneric.isWildcard()) {
                    resolveType(firstGeneric, isParameter);
                }
            } else if (qualifiedName.equals("io.github.eduoneproject.eduone.query.common.domain.DataOption")) {
                ResolvedType firstGeneric = getFirstGeneric(resolvedType);
                if (!firstGeneric.isWildcard()) {
                    resolveType(firstGeneric, isParameter);
                }
            } else {
                if (processedTypeNameSet.contains(qualifiedName)) {
                    return;
                }
                processedTypeNameSet.add(qualifiedName);

                GraphqlInputTypeInfoModel inputTypeInfoModel = new GraphqlInputTypeInfoModel();
                inputTypeInfoModel.setName(getResolvedTypeModel(resolvedType).toString());
                inputTypeInfoModel.setQualifiedName(qualifiedName);
                inputTypeInfoModel.setParameter(isParameter);

                // 模型注释
                TypeDeclaration<?> typeDeclaration = compilationUnitList.stream()
                        .flatMap(compilationUnit -> compilationUnit.getTypes().stream())
                        .filter(item -> {
                            ResolvedReferenceTypeDeclaration resolve = item.resolve();
                            String resolveQualifiedName = resolve.getQualifiedName();
                            return Objects.equals(resolveQualifiedName, qualifiedName);
                        })
                        .findFirst()
                        .orElse(null);
                if (typeDeclaration != null) {
                    Optional<Comment> commentOptional = typeDeclaration.getComment();
                    if (commentOptional.isPresent()) {
                        Comment comment = commentOptional.get();
                        if (!comment.isJavadocComment()) {
                            throw new RuntimeException("不支持的注释类型" + comment);
                        }
                        JavadocComment javadocComment = comment.asJavadocComment();
                        Javadoc javadoc = javadocComment.parse();
                        JavadocDescription javadocDescription = javadoc.getDescription();
                        inputTypeInfoModel.setComment(javadocDescription.toText());
                    }
                }

                // 模型字段
                List<GraphqlFieldInfoModel> fieldInfoModelList = new ArrayList<>();
                Optional<ResolvedReferenceTypeDeclaration> typeDeclarationOptional = resolvedReferenceType.getTypeDeclaration();
                if (typeDeclarationOptional.isPresent()) {
                    ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration = typeDeclarationOptional.get();
                    List<ResolvedFieldDeclaration> allNonStaticFields = resolvedReferenceTypeDeclaration.getAllNonStaticFields();
                    for (ResolvedFieldDeclaration allNonStaticField : allNonStaticFields) {
                        GraphqlFieldInfoModel graphqlFieldInfoModel = new GraphqlFieldInfoModel();

                        // 字段名称
                        graphqlFieldInfoModel.setParameterName(allNonStaticField.getName());

                        // 字段类型
                        ResolvedType type = allNonStaticField.getType();
                        resolveType(type, isParameter);
                        graphqlFieldInfoModel.setTypeName(getResolvedTypeModel(type).toString());

                        // 字段注释
                        FieldDeclaration wrappedNode = ((JavaParserFieldDeclaration) allNonStaticField).getWrappedNode();
                        Optional<Comment> commentOptional = wrappedNode.getComment();
                        if (commentOptional.isPresent()) {
                            Comment comment = commentOptional.get();
                            if (!comment.isJavadocComment()) {
                                throw new RuntimeException("不支持的注释类型" + comment);
                            }
                            JavadocComment javadocComment = comment.asJavadocComment();
                            Javadoc javadoc = javadocComment.parse();
                            JavadocDescription javadocDescription = javadoc.getDescription();
                            graphqlFieldInfoModel.setComment(javadocDescription.toText());
                        }
                        fieldInfoModelList.add(graphqlFieldInfoModel);
                    }
                }
                inputTypeInfoModel.setFieldInfoModelList(fieldInfoModelList);

                graphqlInputTypeInfoModelMap.put(resolvedTypeName, inputTypeInfoModel);
            }
        }
    }

    /**
     * 获取gql类型名称
     *
     * @param resolvedType 类型
     * @return gql类型名称
     */
    private FieldTypeInfoModel getResolvedTypeModel(ResolvedType resolvedType) {
        if (resolvedType.isReferenceType()) {
            ResolvedReferenceType referenceType = resolvedType.asReferenceType();
            String fieldQualifiedName = referenceType.getQualifiedName();
            if (Objects.equals(fieldQualifiedName, "java.lang.String")) {
                return FieldTypeInfoModel.of("String");
            } else if (Objects.equals(fieldQualifiedName, "java.lang.Integer")) {
                return FieldTypeInfoModel.of("Int");
            } else if (Objects.equals(fieldQualifiedName, "java.lang.Boolean")) {
                return FieldTypeInfoModel.of("Boolean");
            } else if (Objects.equals(fieldQualifiedName, "java.util.Date")) {
                return FieldTypeInfoModel.of("DateTime");
            } else if (Objects.equals(fieldQualifiedName, "java.util.List")) { // 列表模型
                ResolvedType firstGeneric = getFirstGeneric(resolvedType);
                FieldTypeInfoModel resolvedTypeModel = getResolvedTypeModel(firstGeneric);
                String typeName = "[" + resolvedTypeModel.getTypeName() + "]";
                return FieldTypeInfoModel.of(typeName, null, resolvedTypeModel.getDirectiveInfoSet());
            } else if (Objects.equals(fieldQualifiedName, "io.github.eduoneproject.eduone.query.common.domain.DataOption")) { // 数据选项模型
                ResolvedType firstGeneric = getFirstGeneric(resolvedType);
                FieldTypeInfoModel resolvedTypeModel = getResolvedTypeModel(firstGeneric);
                String typeName = resolvedTypeModel.getTypeName() + "DataOption";
                String directive = "@dataOption(for: \"" + resolvedTypeModel.getTypeName() + "\")";
                return FieldTypeInfoModel.of(typeName, directive, resolvedTypeModel.getDirectiveInfoSet());
            } else if (Objects.equals(fieldQualifiedName, "io.github.eduoneproject.eduone.common.domain.DataScopeRequest")) { // 数据选项模型
                ResolvedType firstGeneric = getFirstGeneric(resolvedType);
                FieldTypeInfoModel resolvedTypeModel = getResolvedTypeModel(firstGeneric);
                String typeName = resolvedTypeModel.getTypeName() + "ScopeRequest";
                String directive = "@scopeRequest(for: \"" + resolvedTypeModel.getTypeName() + "\")";
                return FieldTypeInfoModel.of(typeName, directive, resolvedTypeModel.getDirectiveInfoSet());
            } else if (Objects.equals(fieldQualifiedName, "io.github.eduoneproject.eduone.business.gateway.response.CommonBusinessResponse")) { // 通用业务响应
                ResolvedType firstGeneric = getFirstGeneric(resolvedType);
                if (firstGeneric.isWildcard()) {
                    return FieldTypeInfoModel.of("CommonBusinessResponse");
                } else {
                    FieldTypeInfoModel resolvedTypeModel = getResolvedTypeModel(firstGeneric);
                    String typeName = resolvedTypeModel.getTypeName() + "BusinessResponse";
                    String directive = "@businessResponse(for: \"" + resolvedTypeModel.getTypeName() + "\")";
                    return FieldTypeInfoModel.of(typeName, directive, resolvedTypeModel.getDirectiveInfoSet());
                }
            } else if (Objects.equals(fieldQualifiedName, "io.github.eduoneproject.eduone.common.domain.DataPage")) { // 分页模型
                ResolvedType firstGeneric = getFirstGeneric(resolvedType);
                if (firstGeneric.isWildcard()) {
                    return FieldTypeInfoModel.of("DataPage");
                } else {
                    FieldTypeInfoModel resolvedTypeModel = getResolvedTypeModel(firstGeneric);
                    String typeName = resolvedTypeModel.getTypeName() + "Page";
                    String directive = "@page(for: \"" + resolvedTypeModel.getTypeName() + "\")";
                    return FieldTypeInfoModel.of(typeName, directive, resolvedTypeModel.getDirectiveInfoSet());
                }
            } else {
                Optional<ResolvedReferenceTypeDeclaration> typeDeclarationOptional = referenceType.getTypeDeclaration();
                if (typeDeclarationOptional.isEmpty()) {
                    throw new RuntimeException("不支持的类型" + resolvedType);
                }
                ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration = typeDeclarationOptional.get();
                return FieldTypeInfoModel.of(resolvedReferenceTypeDeclaration.getName());
            }
        } else if (resolvedType.isPrimitive()) {
            ResolvedPrimitiveType typePrimitive = resolvedType.asPrimitive();
            if (typePrimitive.isBoolean()) {
                return FieldTypeInfoModel.of("Boolean");
            }
        }
        throw new RuntimeException("不支持的类型" + resolvedType);
    }
}
