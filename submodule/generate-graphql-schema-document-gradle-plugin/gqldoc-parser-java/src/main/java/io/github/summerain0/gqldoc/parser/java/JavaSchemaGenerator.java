package io.github.summerain0.gqldoc.parser.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import io.github.summerain0.gqldoc.core.enums.OperationType;
import io.github.summerain0.gqldoc.core.enums.TypeKind;
import io.github.summerain0.gqldoc.core.extension.ExtensionRegistry;
import io.github.summerain0.gqldoc.core.ir.*;
import io.github.summerain0.gqldoc.core.resolver.TypeDefContext;
import io.github.summerain0.gqldoc.parser.java.bean.ModuleArtifactInfo;
import io.github.summerain0.gqldoc.parser.java.consts.ClassNameConstants;
import io.github.summerain0.gqldoc.parser.java.util.ClassUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 适用于Java语言的Schema生成器
 *
 * @author summerain0
 */
public class JavaSchemaGenerator {
    /**
     * 项目类加载器
     */
    private final ClassLoader classLoader;

    /**
     * 控制器的限定类名列表
     */
    private final List<String> controllerQualifiedClassNameList;

    /**
     * 扩展注册表
     */
    private final ExtensionRegistry extensionRegistry;

    /**
     * 类型定义缓存
     * <p>kv定义：&lt;qualifiedName, TypeKind> -> TypeDef</p>
     */
    private Map<Pair<String, TypeKind>, TypeDef> cacheTypeDefMap = new HashMap<>();

    /**
     * 已解析完成的类型引用缓存
     * <p>kv定义：&lt;Java类全限定名, TypeKind> -> TypeRef</p>
     */
    private Map<Pair<String, TypeKind>, TypeRef> resolvedTypeRefMap = new HashMap<>();

    /**
     * 正在解析中的类型
     * <p>用于处理自嵌套或循环引用，避免无限递归导致栈溢出</p>
     */
    private Set<Pair<String, TypeKind>> resolvingTypeSet = new HashSet<>();

    /**
     * Java源码解析器
     */
    private final JavaSourceCodeResolver javaSourceCodeResolver;

    /**
     * Java解析器
     */
    private final JavaParser javaParser;

    /**
     * 需要忽略字段的类型
     */
    private final Class<?>[] IGNORE_FIELD_CLASS = {
            int.class, long.class, float.class, double.class, boolean.class,
            String.class, Date.class, OffsetDateTime.class
    };

    /**
     * 用于匹配@see注释中的类引用的正则表达式
     */
    private static final Pattern SEE_TAG_CLASS_REFERENCE_PATTERN = Pattern.compile("[A-Za-z_$][\\w$]*(?:\\.[A-Za-z_$][\\w$]*)*");

    /**
     * 用于匹配Javadoc内联标签的正则表达式
     */
    private static final Pattern JAVADOC_INLINE_TAG_PATTERN = Pattern.compile("\\{@[^}]*}");

    /**
     * 构造函数
     *
     * @param projectSrcDirFiles     项目源码目录文件列表
     * @param moduleArtifactInfoList 模块工件文件目录文件列表
     */
    public JavaSchemaGenerator(List<File> projectSrcDirFiles, List<ModuleArtifactInfo> moduleArtifactInfoList) {
        if (CollectionUtils.isEmpty(projectSrcDirFiles)) {
            throw new RuntimeException("项目源码目录文件列表为空");
        }
        if (CollectionUtils.isEmpty(moduleArtifactInfoList)) {
            throw new RuntimeException("模块工件文件目录文件列表为空");
        }

        // 构建classLoader
        List<URI> classesDirUriList = moduleArtifactInfoList.stream()
                .map(ModuleArtifactInfo::getClassesJarFiles)
                .flatMap(Collection::stream)
                .map(File::toURI)
                .toList();
        URL[] urls = classesDirUriList.stream().map(e -> {
            try {
                return e.toURL();
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        }).toArray(URL[]::new);
        this.classLoader = new URLClassLoader(urls);

        // 初始化Java解析器
        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_21);
        this.javaParser = new JavaParser(configuration);

        // 处理一下控制器
        this.controllerQualifiedClassNameList = getControllerQualifiedClassNameList(projectSrcDirFiles);

        // 注册扩展
        this.extensionRegistry = ExtensionRegistry.getInstance();

        // 初始化Java源码解析器
        this.javaSourceCodeResolver = JavaSourceCodeResolver.getInstance(projectSrcDirFiles);
    }

    /**
     * 生成Schema
     */
    public List<SchemaIR> generate() throws Exception {
        List<SchemaIR> schemaIRList = new ArrayList<>();
        for (String qualifiedClassName : controllerQualifiedClassNameList) {
            cacheTypeDefMap = new HashMap<>();
            resolvedTypeRefMap = new HashMap<>();
            resolvingTypeSet = new HashSet<>();

            SchemaIR.SchemaIRBuilder schemaIRBuilder = SchemaIR.builder()
                    .schemaName(ClassUtils.getSimpleName(qualifiedClassName));

            Class<?> controllerClass = classLoader.loadClass(qualifiedClassName);

            // 先检查一下是不是符合要求的控制器
            checkController(controllerClass);

            List<OperationField> queryOperationFieldList = new ArrayList<>();
            List<OperationField> mutationOperationFieldList = new ArrayList<>();

            Method[] methods = controllerClass.getMethods();
            for (Method method : methods) {
                OperationType operationType = getOperationType(method);
                if (operationType == null) {
                    continue;
                }

                // 入参
                Map<String, String> paramCommentMap = javaSourceCodeResolver.getMethodParamComments(qualifiedClassName, method.getName());
                List<ArgumentDef> argumentDefList = new ArrayList<>();
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    if (!existsArgumentsAnnotation(parameter)) {
                        continue;
                    }
                    String parameterName = parameter.getName();
                    Type parameterType = parameter.getParameterizedType();
                    TypeRef typeRef = resolveType(parameterType, false);
                    ArgumentDef argumentDef = ArgumentDef.builder()
                            .name(parameterName)
                            .description(paramCommentMap.get(parameterName))
                            .type(typeRef)
                            .build();
                    argumentDefList.add(argumentDef);
                }
                // 返回值
                Type returnType = method.getGenericReturnType();
                TypeRef returnTypeRef = resolveType(returnType, true);

                OperationField operationField = OperationField.builder()
                        .name(method.getName())
                        .description(javaSourceCodeResolver.getMethodComment(qualifiedClassName, method.getName()))
                        .seeTags(resolveSeeTags(
                                qualifiedClassName,
                                javaSourceCodeResolver.getMethodSeeTags(qualifiedClassName, method.getName())))
                        .returnType(returnTypeRef)
                        .returnDescription(javaSourceCodeResolver.getMethodReturnComment(qualifiedClassName, method.getName()))
                        .arguments(argumentDefList)
                        .build();
                if (operationType == OperationType.QUERY) {
                    queryOperationFieldList.add(operationField);
                } else if (operationType == OperationType.MUTATION) {
                    mutationOperationFieldList.add(operationField);
                } else {
                    throw new RuntimeException("不支持的操作类型：" + operationType);
                }
            }

            schemaIRBuilder.queryFields(queryOperationFieldList);
            schemaIRBuilder.mutationFields(mutationOperationFieldList);
            schemaIRBuilder.definitions(resolveTypeDefMap(cacheTypeDefMap));
            schemaIRList.add(schemaIRBuilder.build());
        }
        return schemaIRList;
    }

    /**
     * 解析类型
     *
     * @param type 类型
     */
    private TypeRef resolveType(Type type, boolean isReturnType) {
        if (type == null) {
            return null;
        }

        if (type instanceof ParameterizedType parameterizedType) { // 含泛型的类
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class<?> rawClass) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                // 处理一下内部泛型
                List<TypeRef> genericArgs = new ArrayList<>();
                for (Type actualTypeArgument : actualTypeArguments) {
                    TypeRef typeRef = resolveType(actualTypeArgument, isReturnType);
                    if (typeRef != null) {
                        genericArgs.add(typeRef);
                    }
                }

                TypeRef typeRef;
                if (Collection.class.isAssignableFrom(rawClass)) { // 集合类型
                    typeRef = extensionRegistry.transformRef(Collection.class.getTypeName(), genericArgs);
                } else { // 其他类型
                    typeRef = extensionRegistry.transformRef(rawType.getTypeName(), genericArgs);
                }

                if (typeRef == null) {
                    throw new RuntimeException("不支持的类型：" + rawType.getTypeName());
                }
                return typeRef;
            }
        } else if (type instanceof Class<?> typeClass) { // 普通类
            if (Modifier.isStatic(typeClass.getModifiers())) {
                return null;
            }

            if (typeClass == Void.class) {
                return null;
            }

            TypeKind typeKind = typeClass.isEnum() ? TypeKind.ENUM : (isReturnType ? TypeKind.OBJECT : TypeKind.INPUT);
            Pair<String, TypeKind> typeRefCacheKey = Pair.of(typeClass.getName(), typeKind);

            // 已解析完成，直接复用类型引用
            TypeRef resolvedTypeRef = resolvedTypeRefMap.get(typeRefCacheKey);
            if (resolvedTypeRef != null) {
                return resolvedTypeRef;
            }

            // 自嵌套或循环引用：该类型正在解析中，先返回引用，待外层解析完成后注册类型定义
            if (resolvingTypeSet.contains(typeRefCacheKey)) {
                return extensionRegistry.transformRef(type.getTypeName(), null);
            }

            resolvingTypeSet.add(typeRefCacheKey);
            try {
                TypeDef typeDef;

                // 类型定义模型上下文
                TypeDefContext typeDefContext = new TypeDefContext();

                if (typeClass.isEnum()) { // 枚举
                    typeDefContext.setTypeKind(TypeKind.ENUM);
                    List<FieldDef> fields = new ArrayList<>();
                    Object[] enumConstants = typeClass.getEnumConstants();
                    for (Object constant : enumConstants) {
                        FieldDef fieldDef = FieldDef.builder()
                                .name(constant.toString())
                                .description(buildDescription(
                                        javaSourceCodeResolver.getFieldComment(typeClass.getName(), constant.toString()),
                                        typeClass.getName(),
                                        javaSourceCodeResolver.getFieldSeeTags(typeClass.getName(), constant.toString())))
                                .build();
                        fields.add(fieldDef);
                    }
                    typeDefContext.setFieldDefList(fields);
                } else { // 普通对象
                    typeDefContext.setTypeKind(typeKind);

                    if (!isIgnoreFieldClass(typeClass)) {
                        Field[] declaredFields = typeClass.getDeclaredFields();
                        List<FieldDef> fieldFieldDefList = new ArrayList<>();
                        for (Field declaredField : declaredFields) {
                            if (declaredField.isSynthetic() || Modifier.isStatic(declaredField.getModifiers())) {
                                continue;
                            }
                            Type genericType = declaredField.getGenericType();
                            TypeRef typeRef = resolveType(genericType, isReturnType);
                            FieldDef fieldDef = FieldDef.builder()
                                    .name(declaredField.getName())
                                    .description(buildDescription(
                                            javaSourceCodeResolver.getFieldComment(typeClass.getName(), declaredField.getName()),
                                            typeClass.getName(),
                                            javaSourceCodeResolver.getFieldSeeTags(typeClass.getName(), declaredField.getName())))
                                    .type(typeRef)
                                    .build();
                            fieldFieldDefList.add(fieldDef);
                        }
                        typeDefContext.setFieldDefList(fieldFieldDefList);
                    }
                }

                typeDefContext.setDescription(buildDescription(
                        javaSourceCodeResolver.getClassComment(typeClass.getName()),
                        typeClass.getName(),
                        javaSourceCodeResolver.getClassSeeTags(typeClass.getName())));
                typeDef = extensionRegistry.transformDef(type.getTypeName(), typeDefContext);
                cacheTypeDefMap.put(Pair.of(typeDef.getName(), typeDef.getKind()), typeDef);
                TypeRef typeRef = extensionRegistry.transformRef(type.getTypeName(), null);
                resolvedTypeRefMap.put(typeRefCacheKey, typeRef);
                return typeRef;
            } finally {
                resolvingTypeSet.remove(typeRefCacheKey);
            }
        }
        throw new RuntimeException("不支持的类型：" + type.getTypeName());
    }

    /**
     * 获取控制器的限定类名
     *
     * @param projectSrcDirFiles 项目源码目录文件
     * @return 限定类名列表
     */
    private List<String> getControllerQualifiedClassNameList(List<File> projectSrcDirFiles) {
        List<String> qualifiedClassName = new ArrayList<>();
        for (File projectSrcDirFile : projectSrcDirFiles) {
            if (projectSrcDirFile.exists()) {
                try (Stream<Path> pathStream = Files.walk(projectSrcDirFile.toPath())) {
                    pathStream.forEach(path -> {
                        try {
                            if (!Files.isDirectory(path)) {
                                ParseResult<CompilationUnit> parseResult = javaParser.parse(path);
                                if (parseResult.isSuccessful()) {
                                    Optional<CompilationUnit> compilationUnitOptional = parseResult.getResult();
                                    if (compilationUnitOptional.isPresent()) {
                                        CompilationUnit compilationUnit = compilationUnitOptional.get();
                                        List<AnnotationExpr> annotationExprList = compilationUnit.findAll(AnnotationExpr.class);
                                        for (AnnotationExpr annotationExpr : annotationExprList) {
                                            if (Objects.equals(annotationExpr.getNameAsString(), ClassNameConstants.CONTROLLER)
                                                    || Objects.equals(annotationExpr.getNameAsString(), ClassNameConstants.REST_CONTROLLER)) {
                                                qualifiedClassName.add(getQualifiedClassName(compilationUnit));
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("解析控制器失败！", e);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return qualifiedClassName;
    }

    /**
     * 获取限定类名
     *
     * @param compilationUnit 编译单元
     * @return 限定类名
     */
    private String getQualifiedClassName(CompilationUnit compilationUnit) {
        if (compilationUnit == null) {
            return null;
        }
        String packageName = compilationUnit.getPackageDeclaration()
                .map(PackageDeclaration::getNameAsString)
                .orElse(null);
        String className = compilationUnit.getPrimaryType()
                .map(TypeDeclaration::getNameAsString)
                .orElse(null);
        if (className == null) {
            className = compilationUnit.getTypes().stream()
                    .findFirst()
                    .map(TypeDeclaration::getNameAsString)
                    .orElse(null);
        }
        if (className != null) {
            return packageName != null ? packageName + "." + className : className;
        }
        return null;
    }

    /**
     * 检查控制器
     *
     * @param clazz 类
     * @throws RuntimeException 非法的控制器
     */
    private void checkController(Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            String annotationTypeName = annotationType.getName();
            if (Objects.equals(annotationTypeName, ClassNameConstants.CONTROLLER_QUALIFIED) ||
                    Objects.equals(annotationTypeName, ClassNameConstants.REST_CONTROLLER_QUALIFIED)) {
                return;
            }
        }
        throw new RuntimeException(clazz.getName() + "不是一个合法的Graphql控制器");
    }

    /**
     * 获取操作类型
     *
     * @param method 方法
     * @return 操作类型
     */
    private OperationType getOperationType(Method method) {
        if (method == null) {
            return null;
        }
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            String annotationTypeName = annotationType.getName();
            if (Objects.equals(annotationTypeName, ClassNameConstants.QUERY_MAPPING_QUALIFIED)) {
                return OperationType.QUERY;
            } else if (Objects.equals(annotationTypeName, ClassNameConstants.MUTATION_MAPPING_QUALIFIED)) {
                return OperationType.MUTATION;
            }
        }
        return null;
    }

    /**
     * 是否是忽略字段的类型
     *
     * @param clazz 类
     * @return 是否是忽略的字段类型
     */
    private boolean isIgnoreFieldClass(Class<?> clazz) {
        for (Class<?> ignoreFieldClass : IGNORE_FIELD_CLASS) {
            if (ignoreFieldClass.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在参数注解
     *
     * @param parameter 参数
     * @return 是否存在参数注解
     */
    public boolean existsArgumentsAnnotation(Parameter parameter) {
        if (parameter == null) {
            return false;
        }
        return Arrays.stream(parameter.getAnnotations())
                .anyMatch(annotation -> {
                    String annotationTypeName = annotation.annotationType().getName();
                    return Objects.equals(annotationTypeName, ClassNameConstants.ARGUMENT_QUALIFIED);
                });
    }

    /**
     * 构建描述
     * <p>将@see注释追加到描述末尾，其中可解析的类引用会替换为GraphQL类型名称，并作为类型输出到Schema中</p>
     *
     * @param description    原始描述
     * @param ownerClassName 描述所属类的全限定名
     * @param seeTagList     @see注释列表
     * @return 构建后的描述
     */
    private String buildDescription(String description, String ownerClassName, List<String> seeTagList) {
        List<String> resolvedSeeTagList = resolveSeeTags(ownerClassName, seeTagList);
        if (resolvedSeeTagList.isEmpty()) {
            return description;
        }
        StringBuilder descriptionBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(description)) {
            descriptionBuilder.append(description.strip());
            descriptionBuilder.append("\n\n");
        }
        descriptionBuilder.append(resolvedSeeTagList.stream()
                .map(seeTag -> "@see " + seeTag)
                .collect(Collectors.joining("\n")));
        return descriptionBuilder.toString();
    }

    /**
     * 解析@see注释列表
     *
     * @param ownerClassName 描述所属类的全限定名
     * @param seeTagList     @see注释列表
     * @return 解析后的@see注释列表
     */
    private List<String> resolveSeeTags(String ownerClassName, List<String> seeTagList) {
        if (CollectionUtils.isEmpty(seeTagList)) {
            return Collections.emptyList();
        }
        List<String> resolvedSeeTagList = new ArrayList<>();
        for (String seeTag : seeTagList) {
            if (StringUtils.isBlank(seeTag)) {
                continue;
            }
            resolvedSeeTagList.add(resolveSeeTag(ownerClassName, seeTag.strip()));
        }
        return resolvedSeeTagList;
    }

    /**
     * 解析@see注释
     * <p>仅替换可解析为GraphQL类型的类引用，其余内容原样输出；Javadoc内联标签不处理</p>
     *
     * @param ownerClassName 描述所属类的全限定名
     * @param seeTag         @see注释内容
     * @return 解析后的@see注释内容
     */
    private String resolveSeeTag(String ownerClassName, String seeTag) {
        List<int[]> inlineTagRangeList = new ArrayList<>();
        Matcher inlineTagMatcher = JAVADOC_INLINE_TAG_PATTERN.matcher(seeTag);
        while (inlineTagMatcher.find()) {
            inlineTagRangeList.add(new int[]{inlineTagMatcher.start(), inlineTagMatcher.end()});
        }

        Matcher classReferenceMatcher = SEE_TAG_CLASS_REFERENCE_PATTERN.matcher(seeTag);
        StringBuilder seeTagBuilder = new StringBuilder();
        int lastEnd = 0;
        while (classReferenceMatcher.find()) {
            seeTagBuilder.append(seeTag, lastEnd, classReferenceMatcher.start());
            String classReference = classReferenceMatcher.group();
            String replacement = classReference;
            if (!isInsideRange(classReferenceMatcher.start(), inlineTagRangeList)) {
                String resolvedTypeName = resolveSeeTagTypeName(ownerClassName, classReference);
                if (resolvedTypeName != null) {
                    replacement = resolvedTypeName;
                }
            }
            seeTagBuilder.append(replacement);
            lastEnd = classReferenceMatcher.end();
        }
        seeTagBuilder.append(seeTag.substring(lastEnd));
        return seeTagBuilder.toString();
    }

    /**
     * 是否位于指定区间内
     *
     * @param index     下标
     * @param rangeList 区间列表
     * @return 是否位于区间内
     */
    private boolean isInsideRange(int index, List<int[]> rangeList) {
        for (int[] range : rangeList) {
            if (index >= range[0] && index < range[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解析@see注释中的类引用为GraphQL类型名称
     * <p>无法在项目源码中找到对应类或无法映射为GraphQL类型时返回null，按普通文本注释处理</p>
     *
     * @param ownerClassName 描述所属类的全限定名
     * @param classReference 类引用
     * @return GraphQL类型名称
     */
    private String resolveSeeTagTypeName(String ownerClassName, String classReference) {
        String lastSegment = classReference.contains(".")
                ? StringUtils.substringAfterLast(classReference, ".")
                : classReference;
        if (StringUtils.isBlank(lastSegment) || !Character.isUpperCase(lastSegment.charAt(0))) {
            return null;
        }
        for (String qualifiedClassName : javaSourceCodeResolver.resolveClassReferenceCandidates(ownerClassName, classReference)) {
            if (!javaSourceCodeResolver.containsClass(qualifiedClassName)) {
                continue;
            }
            Class<?> clazz;
            try {
                clazz = Class.forName(qualifiedClassName, false, classLoader);
            } catch (ClassNotFoundException | LinkageError e) {
                continue;
            }
            if (!isSeeTagTypeClass(clazz)) {
                continue;
            }
            try {
                TypeRef typeRef = resolveType(clazz, true);
                if (typeRef != null && StringUtils.isNotBlank(typeRef.getName())) {
                    return typeRef.getName();
                }
            } catch (RuntimeException ignored) {
                // 无法映射为GraphQL类型，按普通文本注释处理
            }
            return null;
        }
        return null;
    }

    /**
     * 是否是可以作为类型输出到Schema的类
     *
     * @param clazz 类
     * @return 是否可以作为类型输出
     */
    private boolean isSeeTagTypeClass(Class<?> clazz) {
        if (clazz.isPrimitive() || clazz.isArray() || clazz.isInterface() || clazz == Object.class) {
            return false;
        }
        if (clazz.getEnclosingClass() != null) {
            return false;
        }
        if (Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)) {
            return false;
        }
        if (clazz.getTypeParameters().length > 0) {
            return false;
        }
        if (clazz.isEnum()) {
            Object[] enumConstants = clazz.getEnumConstants();
            return enumConstants != null && enumConstants.length > 0;
        }
        if (isIgnoreFieldClass(clazz)) {
            return true;
        }
        return Arrays.stream(clazz.getDeclaredFields())
                .anyMatch(field -> !field.isSynthetic() && !Modifier.isStatic(field.getModifiers()));
    }

    /**
     * 解析类型定义映射
     *
     * @param typeDefMap 类型定义映射
     * @return 解析后的类型定义映射
     */
    private Map<String, TypeDef> resolveTypeDefMap(Map<Pair<String, TypeKind>, TypeDef> typeDefMap) {
        Collection<TypeDef> typeDefList = typeDefMap.values();
        Collection<String> typeDefNameList = typeDefList.stream().map(TypeDef::getName).toList();
        // 检查是否有重复名称的
        Set<String> duplicates = typeDefNameList.stream()
                .filter(e -> Collections.frequency(typeDefNameList, e) > 1)
                .collect(Collectors.toSet());
        if (!duplicates.isEmpty()) {
            throw new RuntimeException("类型定义名称重复：" + duplicates);
        }

        return typeDefList.stream().collect(Collectors.toMap(TypeDef::getName, t -> t));
    }
}
