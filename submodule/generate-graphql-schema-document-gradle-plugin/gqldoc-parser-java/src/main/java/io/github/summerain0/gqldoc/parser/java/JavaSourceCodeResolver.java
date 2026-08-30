package io.github.summerain0.gqldoc.parser.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.description.JavadocDescription;
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
        configuration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
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
                    // 类和接口
                    List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationList = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
                    for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarationList) {
                        String qualifiedName;
                        if (StringUtils.isNotBlank(packageName)) {
                            qualifiedName = packageName + "." + classOrInterfaceDeclaration.getNameAsString();
                        } else {
                            qualifiedName = classOrInterfaceDeclaration.getNameAsString();
                        }
                        if (Objects.equals(qualifiedName, className)) {
                            return classOrInterfaceDeclaration.getJavadocComment()
                                    .map(JavadocComment::parse)
                                    .map(Javadoc::getDescription)
                                    .map(JavadocDescription::toText)
                                    .orElse(null);
                        }
                    }
                    // 枚举
                    List<EnumDeclaration> enumDeclarationList = compilationUnit.findAll(EnumDeclaration.class);
                    for (EnumDeclaration enumDeclaration : enumDeclarationList) {
                        String qualifiedName;
                        if (StringUtils.isNotBlank(packageName)) {
                            qualifiedName = packageName + "." + enumDeclaration.getNameAsString();
                        } else {
                            qualifiedName = enumDeclaration.getNameAsString();
                        }
                        if (Objects.equals(qualifiedName, className)) {
                            return enumDeclaration.getJavadocComment()
                                    .map(JavadocComment::parse)
                                    .map(Javadoc::getDescription)
                                    .map(JavadocDescription::toText)
                                    .orElse(null);
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
     * 获取方法注释
     *
     * @param className  类名
     * @param methodName 方法名
     * @return 方法注释
     */
    public String getMethodComment(String className, String methodName) {
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
                                            .map(Javadoc::getDescription)
                                            .map(JavadocDescription::toText)
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
                    // 类和接口
                    List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationList = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
                    for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarationList) {
                        String qualifiedName;
                        if (StringUtils.isNotBlank(packageName)) {
                            qualifiedName = packageName + "." + classOrInterfaceDeclaration.getNameAsString();
                        } else {
                            qualifiedName = classOrInterfaceDeclaration.getNameAsString();
                        }
                        if (Objects.equals(qualifiedName, className)) {
                            List<FieldDeclaration> fieldDeclarationList = classOrInterfaceDeclaration.findAll(FieldDeclaration.class);
                            for (FieldDeclaration fieldDeclaration : fieldDeclarationList) {
                                NodeList<VariableDeclarator> variableDeclaratorNodeList = fieldDeclaration.getVariables();
                                for (VariableDeclarator variableDeclarator : variableDeclaratorNodeList) {
                                    SimpleName simpleName = variableDeclarator.getName();
                                    String name = simpleName.asString();
                                    if (Objects.equals(name, fieldName)) {
                                        return fieldDeclaration.getJavadocComment()
                                                .map(JavadocComment::parse)
                                                .map(Javadoc::getDescription)
                                                .map(JavadocDescription::toText)
                                                .orElse(null);
                                    }
                                }
                            }
                        }
                    }
                    // 枚举
                    List<EnumDeclaration> enumDeclarationList = compilationUnit.findAll(EnumDeclaration.class);
                    for (EnumDeclaration enumDeclaration : enumDeclarationList) {
                        String qualifiedName;
                        if (StringUtils.isNotBlank(packageName)) {
                            qualifiedName = packageName + "." + enumDeclaration.getNameAsString();
                        } else {
                            qualifiedName = enumDeclaration.getNameAsString();
                        }
                        if (Objects.equals(qualifiedName, className)) {
                            List<EnumConstantDeclaration> enumConstantDeclarationList = enumDeclaration.findAll(EnumConstantDeclaration.class);
                            for (EnumConstantDeclaration enumConstantDeclaration : enumConstantDeclarationList) {
                                if (Objects.equals(enumConstantDeclaration.getNameAsString(), fieldName)) {
                                    return enumConstantDeclaration.getJavadocComment()
                                            .map(JavadocComment::parse)
                                            .map(Javadoc::getDescription)
                                            .map(JavadocDescription::toText)
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
