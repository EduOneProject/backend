package io.github.summerain0.gqldoc.parser.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * Java源码解析器
 *
 * @author summerain0
 */
public class JavaSourceCodeResolver {
    private static volatile JavaSourceCodeResolver INSTANCE;

    /**
     * 全限定名到文件路径映射
     */
    private final Map<String, String> qualifiedNameToPathMap = new HashMap<>();

    /**
     * 项目源码目录文件列表
     */
    private final List<File> projectSrcDirFiles;

    /**
     * Java解析器
     */
    private final JavaParser javaParser;

    /**
     * 构造函数
     *
     * @param projectSrcDirFiles 项目源码目录文件列表
     */
    public JavaSourceCodeResolver(List<File> projectSrcDirFiles) {
        this.projectSrcDirFiles = projectSrcDirFiles;

        // 初始化Java解析器
        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_21);
        this.javaParser = new JavaParser(configuration);

        for (File projectSrcDirFile : projectSrcDirFiles) {
            if (!projectSrcDirFile.exists()) {
                continue;
            }
            try (Stream<Path> pathStream = Files.walk(projectSrcDirFile.toPath())) {
                pathStream.forEach(path -> {
                    try {
                        if (Files.isRegularFile(path) && path.toString().endsWith(".java")) {
                            ParseResult<CompilationUnit> parseResult = javaParser.parse(path);
                            if (parseResult.isSuccessful()) {
                                Optional<CompilationUnit> compilationUnitOptional = parseResult.getResult();
                                if (compilationUnitOptional.isPresent()) {
                                    CompilationUnit compilationUnit = compilationUnitOptional.get();
                                    // 处理类和接口的
                                    List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationList = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
                                    String packageName = compilationUnit.getPackageDeclaration()
                                            .map(PackageDeclaration::getName)
                                            .map(Name::asString)
                                            .orElse(null);
                                    for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarationList) {
                                        if (StringUtils.isNotBlank(packageName)) {
                                            qualifiedNameToPathMap.put(packageName + "." + classOrInterfaceDeclaration.getNameAsString(), path.toString());
                                        } else {
                                            qualifiedNameToPathMap.put(classOrInterfaceDeclaration.getNameAsString(), path.toString());
                                        }
                                    }
                                    // 处理枚举
                                    List<EnumDeclaration> enumDeclarationList = compilationUnit.findAll(EnumDeclaration.class);
                                    for (EnumDeclaration enumDeclaration : enumDeclarationList) {
                                        if (StringUtils.isNotBlank(packageName)) {
                                            qualifiedNameToPathMap.put(packageName + "." + enumDeclaration.getNameAsString(), path.toString());
                                        } else {
                                            qualifiedNameToPathMap.put(enumDeclaration.getNameAsString(), path.toString());
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取类注释
     *
     * @param className 类名
     * @return 类注释
     */
    public String getClassComment(String className) {
        Javadoc javadoc = getClassJavadoc(className);
        if (javadoc == null) {
            return null;
        }
        return javadoc.getDescription().toText();
    }

    /**
     * 获取类@see注释
     *
     * @param className 类名
     * @return @see注释列表
     */
    public List<String> getClassSeeTags(String className) {
        return getSeeTags(getClassJavadoc(className));
    }

    /**
     * 获取方法@see注释
     *
     * @param className  类名
     * @param methodName 方法名
     * @return @see注释列表
     */
    public List<String> getMethodSeeTags(String className, String methodName) {
        return getSeeTags(getMethodJavadoc(className, methodName));
    }

    /**
     * 获取字段@see注释
     *
     * @param className 类名
     * @param fieldName 字段名
     * @return @see注释列表
     */
    public List<String> getFieldSeeTags(String className, String fieldName) {
        return getSeeTags(getFieldJavadoc(className, fieldName));
    }

    /**
     * 是否包含指定全限定名的类
     *
     * @param qualifiedClassName 全限定类名
     * @return 是否包含
     */
    public boolean containsClass(String qualifiedClassName) {
        return qualifiedNameToPathMap.containsKey(qualifiedClassName);
    }

    /**
     * 解析类引用候选全限定名
     * <p>按原始引用、显式导入、同包、通配符导入、java.lang的顺序返回候选</p>
     *
     * @param ownerClassName 引用所在类的全限定名
     * @param referenceName  类引用名称
     * @return 候选全限定类名列表
     */
    public List<String> resolveClassReferenceCandidates(String ownerClassName, String referenceName) {
        if (StringUtils.isBlank(referenceName)) {
            return Collections.emptyList();
        }
        Set<String> candidateSet = new LinkedHashSet<>();
        if (referenceName.contains(".")) {
            candidateSet.add(referenceName);
            String firstSegment = StringUtils.substringBefore(referenceName, ".");
            String remainingSegment = StringUtils.substringAfter(referenceName, ".");
            for (String firstSegmentCandidate : resolveSimpleNameCandidates(ownerClassName, firstSegment)) {
                candidateSet.add(firstSegmentCandidate + "." + remainingSegment);
            }
        } else {
            candidateSet.addAll(resolveSimpleNameCandidates(ownerClassName, referenceName));
        }
        return new ArrayList<>(candidateSet);
    }

    /**
     * 获取类Javadoc
     *
     * @param className 类名
     * @return 类Javadoc
     */
    private Javadoc getClassJavadoc(String className) {
        CompilationUnit compilationUnit = getCompilationUnit(className);
        if (compilationUnit == null) {
            return null;
        }
        String packageName = compilationUnit.getPackageDeclaration()
                .map(PackageDeclaration::getName)
                .map(Name::asString)
                .orElse(null);
        // 类和接口
        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationList = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarationList) {
            if (Objects.equals(getQualifiedName(packageName, classOrInterfaceDeclaration.getNameAsString()), className)) {
                return classOrInterfaceDeclaration.getJavadocComment()
                        .map(JavadocComment::parse)
                        .orElse(null);
            }
        }
        // 枚举
        List<EnumDeclaration> enumDeclarationList = compilationUnit.findAll(EnumDeclaration.class);
        for (EnumDeclaration enumDeclaration : enumDeclarationList) {
            if (Objects.equals(getQualifiedName(packageName, enumDeclaration.getNameAsString()), className)) {
                return enumDeclaration.getJavadocComment()
                        .map(JavadocComment::parse)
                        .orElse(null);
            }
        }
        return null;
    }

    /**
     * 获取@see注释
     *
     * @param javadoc Javadoc
     * @return @see注释列表
     */
    private List<String> getSeeTags(Javadoc javadoc) {
        if (javadoc == null) {
            return Collections.emptyList();
        }
        return javadoc.getBlockTags().stream()
                .filter(blockTag -> blockTag.getType() == JavadocBlockTag.Type.SEE)
                .map(blockTag -> blockTag.getContent().toText())
                .filter(StringUtils::isNotBlank)
                .toList();
    }

    /**
     * 获取编译单元
     *
     * @param className 类名
     * @return 编译单元
     */
    private CompilationUnit getCompilationUnit(String className) {
        String classFilePath = qualifiedNameToPathMap.get(className);
        if (classFilePath == null) {
            return null;
        }
        try {
            ParseResult<CompilationUnit> parseResult = javaParser.parse(Path.of(classFilePath));
            if (parseResult.isSuccessful()) {
                return parseResult.getResult().orElse(null);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析简单类名的候选全限定名
     *
     * @param ownerClassName 引用所在类的全限定名
     * @param simpleName     简单类名
     * @return 候选全限定类名列表
     */
    private List<String> resolveSimpleNameCandidates(String ownerClassName, String simpleName) {
        Set<String> candidateSet = new LinkedHashSet<>();
        CompilationUnit compilationUnit = getCompilationUnit(ownerClassName);
        if (compilationUnit != null) {
            // 显式导入
            for (ImportDeclaration importDeclaration : compilationUnit.getImports()) {
                if (importDeclaration.isStatic() || importDeclaration.isAsterisk()) {
                    continue;
                }
                String importName = importDeclaration.getNameAsString();
                if (Objects.equals(StringUtils.substringAfterLast(importName, "."), simpleName)) {
                    candidateSet.add(importName);
                }
            }
            // 同包
            String packageName = compilationUnit.getPackageDeclaration()
                    .map(PackageDeclaration::getName)
                    .map(Name::asString)
                    .orElse(null);
            if (StringUtils.isNotBlank(packageName)) {
                candidateSet.add(packageName + "." + simpleName);
            }
            // 通配符导入
            for (ImportDeclaration importDeclaration : compilationUnit.getImports()) {
                if (importDeclaration.isStatic() || !importDeclaration.isAsterisk()) {
                    continue;
                }
                candidateSet.add(importDeclaration.getNameAsString() + "." + simpleName);
            }
        }
        candidateSet.add("java.lang." + simpleName);
        return new ArrayList<>(candidateSet);
    }

    /**
     * 获取全限定名
     *
     * @param packageName 包名
     * @param simpleName  简单名
     * @return 全限定名
     */
    private static String getQualifiedName(String packageName, String simpleName) {
        return StringUtils.isNotBlank(packageName) ? packageName + "." + simpleName : simpleName;
    }

    /**
     * 获取方法注释
     *
     * @param className  类名
     * @param methodName 方法名
     * @return 方法注释
     */
    public String getMethodComment(String className, String methodName) {
        Javadoc javadoc = getMethodJavadoc(className, methodName);
        if (javadoc == null) {
            return null;
        }
        return javadoc.getDescription().toText();
    }

    /**
     * 获取方法参数注释
     *
     * @param className  类名
     * @param methodName 方法名
     * @return 参数名到参数注释的映射
     */
    public Map<String, String> getMethodParamComments(String className, String methodName) {
        Javadoc javadoc = getMethodJavadoc(className, methodName);
        if (javadoc == null) {
            return Collections.emptyMap();
        }
        Map<String, String> paramCommentMap = new LinkedHashMap<>();
        for (JavadocBlockTag blockTag : javadoc.getBlockTags()) {
            if (blockTag.getType() != JavadocBlockTag.Type.PARAM) {
                continue;
            }
            String paramName = blockTag.getName().orElse(null);
            String paramComment = blockTag.getContent().toText();
            if (StringUtils.isBlank(paramName) || StringUtils.isBlank(paramComment)) {
                continue;
            }
            paramCommentMap.put(paramName, paramComment);
        }
        return paramCommentMap;
    }

    /**
     * 获取方法返回值注释
     *
     * @param className  类名
     * @param methodName 方法名
     * @return 返回值注释
     */
    public String getMethodReturnComment(String className, String methodName) {
        Javadoc javadoc = getMethodJavadoc(className, methodName);
        if (javadoc == null) {
            return null;
        }
        for (JavadocBlockTag blockTag : javadoc.getBlockTags()) {
            if (blockTag.getType() != JavadocBlockTag.Type.RETURN) {
                continue;
            }
            String returnComment = blockTag.getContent().toText();
            if (StringUtils.isNotBlank(returnComment)) {
                return returnComment;
            }
        }
        return null;
    }

    /**
     * 获取方法Javadoc
     *
     * @param className  类名
     * @param methodName 方法名
     * @return 方法Javadoc
     */
    private Javadoc getMethodJavadoc(String className, String methodName) {
        String classFilePath = qualifiedNameToPathMap.get(className);
        if (classFilePath == null) {
            return null;
        }
        try {
            ParseResult<CompilationUnit> parseResult = javaParser.parse(Path.of(classFilePath));
            if (parseResult.isSuccessful()) {
                Optional<CompilationUnit> compilationUnitOptional = parseResult.getResult();
                if (compilationUnitOptional.isPresent()) {
                    CompilationUnit compilationUnit = compilationUnitOptional.get();
                    String packageName = compilationUnit.getPackageDeclaration()
                            .map(PackageDeclaration::getName)
                            .map(Name::asString)
                            .orElse(null);
                    List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationList = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
                    for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarationList) {
                        String qualifiedName;
                        if (StringUtils.isNotBlank(packageName)) {
                            qualifiedName = packageName + "." + classOrInterfaceDeclaration.getNameAsString();
                        } else {
                            qualifiedName = classOrInterfaceDeclaration.getNameAsString();
                        }
                        if (Objects.equals(qualifiedName, className)) {
                            List<MethodDeclaration> methodDeclarationList = classOrInterfaceDeclaration.findAll(MethodDeclaration.class);
                            for (MethodDeclaration methodDeclaration : methodDeclarationList) {
                                if (Objects.equals(methodDeclaration.getNameAsString(), methodName)) {
                                    return methodDeclaration.getJavadocComment()
                                            .map(JavadocComment::parse)
                                            .orElse(null);
                                }
                            }
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取字段注释
     *
     * @param className 类名
     * @param fieldName 字段名
     * @return 字段注释
     */
    public String getFieldComment(String className, String fieldName) {
        Javadoc javadoc = getFieldJavadoc(className, fieldName);
        if (javadoc == null) {
            return null;
        }
        return javadoc.getDescription().toText();
    }

    /**
     * 获取字段Javadoc
     *
     * @param className 类名
     * @param fieldName 字段名
     * @return 字段Javadoc
     */
    private Javadoc getFieldJavadoc(String className, String fieldName) {
        CompilationUnit compilationUnit = getCompilationUnit(className);
        if (compilationUnit == null) {
            return null;
        }
        String packageName = compilationUnit.getPackageDeclaration()
                .map(PackageDeclaration::getName)
                .map(Name::asString)
                .orElse(null);
        // 类和接口
        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationList = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarationList) {
            if (!Objects.equals(getQualifiedName(packageName, classOrInterfaceDeclaration.getNameAsString()), className)) {
                continue;
            }
            List<FieldDeclaration> fieldDeclarationList = classOrInterfaceDeclaration.findAll(FieldDeclaration.class);
            for (FieldDeclaration fieldDeclaration : fieldDeclarationList) {
                NodeList<VariableDeclarator> variableDeclaratorNodeList = fieldDeclaration.getVariables();
                for (VariableDeclarator variableDeclarator : variableDeclaratorNodeList) {
                    SimpleName simpleName = variableDeclarator.getName();
                    if (Objects.equals(simpleName.asString(), fieldName)) {
                        return fieldDeclaration.getJavadocComment()
                                .map(JavadocComment::parse)
                                .orElse(null);
                    }
                }
            }
        }
        // 枚举
        List<EnumDeclaration> enumDeclarationList = compilationUnit.findAll(EnumDeclaration.class);
        for (EnumDeclaration enumDeclaration : enumDeclarationList) {
            if (!Objects.equals(getQualifiedName(packageName, enumDeclaration.getNameAsString()), className)) {
                continue;
            }
            List<EnumConstantDeclaration> enumConstantDeclarationList = enumDeclaration.findAll(EnumConstantDeclaration.class);
            for (EnumConstantDeclaration enumConstantDeclaration : enumConstantDeclarationList) {
                if (Objects.equals(enumConstantDeclaration.getNameAsString(), fieldName)) {
                    return enumConstantDeclaration.getJavadocComment()
                            .map(JavadocComment::parse)
                            .orElse(null);
                }
            }
        }
        return null;
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static JavaSourceCodeResolver getInstance(List<File> projectSrcDirFiles) {
        if (INSTANCE == null) {
            synchronized (JavaSourceCodeResolver.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JavaSourceCodeResolver(projectSrcDirFiles);
                }
            }
        }
        return INSTANCE;
    }
}
