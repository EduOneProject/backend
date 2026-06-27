package io.github.summerain0.plugin.gradle.generator.gqldoc.task;

import io.github.summerain0.plugin.gradle.generator.gqldoc.consts.Constants;
import io.github.summerain0.plugin.gradle.generator.gqldoc.model.GraphqlControllerInfoModel;
import io.github.summerain0.plugin.gradle.generator.gqldoc.model.nested.GraphqlControllerMethodInfoModel;
import io.github.summerain0.plugin.gradle.generator.gqldoc.util.GraphqlDocBuilder;
import io.github.summerain0.plugin.gradle.generator.gqldoc.util.ModuleUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 生成GQL文档任务
 *
 * @author summerain
 */
public abstract class GenerateGraphqlSchemaDocumentTask extends DefaultTask {
    private JavaParser javaParser;

    @TaskAction
    public void generateGraphqlSchemaDocument() throws IOException {
        System.out.println("开始执行GQL文档生成流程...");

        Project currentProject = getProject();
        System.out.printf("所处模块：%s\n", currentProject.getName());

        File projectDirFile = currentProject.getProjectDir();
        File sourceCodeDirFile = new File(projectDirFile, Constants.SOURCE_CODE_DIR);

        System.out.println("当前项目源代码目录：" + sourceCodeDirFile.getAbsolutePath());

        initJavaParser();

        List<CompilationUnit> compilationUnitList = getAllCompilationUnit(sourceCodeDirFile);
        List<CompilationUnit> controllerCompilationUnitList = filterController(compilationUnitList);

        List<GraphqlControllerInfoModel> graphqlControllerInfoModelList = new ArrayList<>();
        for (CompilationUnit compilationUnit : controllerCompilationUnitList) {
            Optional<String> primaryTypeNameOptional = compilationUnit.getPrimaryTypeName();
            if (primaryTypeNameOptional.isEmpty()) {
                continue;
            }
            System.out.println("正在解析控制器：" + primaryTypeNameOptional.get());

            GraphqlControllerInfoModel graphqlControllerInfoModel = new GraphqlControllerInfoModel();
            graphqlControllerInfoModel.setClassName(primaryTypeNameOptional.get());

            List<GraphqlControllerMethodInfoModel> methodInfoModelList = new ArrayList<>();

            // 查询方法
            List<MethodDeclaration> queryMethodDeclarationList = filterGraphqlControllerMethod(compilationUnit, Constants.QUERY_MAPPING);
            for (MethodDeclaration methodDeclaration : queryMethodDeclarationList) {
                List<Parameter> parameterList = filterGraphqlArgument(methodDeclaration);
                GraphqlControllerMethodInfoModel methodInfoModel = new GraphqlControllerMethodInfoModel();
                methodInfoModel.setMethodName(methodDeclaration.getNameAsString());
                methodInfoModel.setMethodDeclaration(methodDeclaration);
                methodInfoModel.setParameterList(parameterList);
                methodInfoModel.setMethodReturnType(methodDeclaration.getType());
                methodInfoModel.setQueryMethod(true);
                methodInfoModelList.add(methodInfoModel);
            }

            // 变更方法
            List<MethodDeclaration> mutationMethodDeclarationList = filterGraphqlControllerMethod(compilationUnit, Constants.MUTATION_MAPPING);
            for (MethodDeclaration methodDeclaration : mutationMethodDeclarationList) {
                List<Parameter> parameterList = filterGraphqlArgument(methodDeclaration);
                GraphqlControllerMethodInfoModel methodInfoModel = new GraphqlControllerMethodInfoModel();
                methodInfoModel.setMethodName(methodDeclaration.getNameAsString());
                methodInfoModel.setMethodDeclaration(methodDeclaration);
                methodInfoModel.setParameterList(parameterList);
                methodInfoModel.setMethodReturnType(methodDeclaration.getType());
                methodInfoModel.setQueryMethod(false);
                methodInfoModelList.add(methodInfoModel);
            }


            graphqlControllerInfoModel.setMethodInfoModelList(methodInfoModelList);
            graphqlControllerInfoModelList.add(graphqlControllerInfoModel);
        }

        System.out.println("预处理完毕，开始解析并生成GQL文档...");

        File outputFile = new File(projectDirFile, Constants.GRAPHQL_DOC_OUTPUT_DIR);
        System.out.println("GQL文档输出目录：" + outputFile.getAbsolutePath());

        GraphqlDocBuilder graphqlDocBuilder = new GraphqlDocBuilder(compilationUnitList);
        graphqlDocBuilder.addAllGraphqlControllerInfoModel(graphqlControllerInfoModelList);
        graphqlDocBuilder.generateGqlDoc();
        graphqlDocBuilder.build(outputFile);

        System.out.println("GQL文档生成完毕！");
    }

    /**
     * 初始化JavaParser
     */
    private void initJavaParser() {
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());

        List<Project> allModuleProject = ModuleUtils.getAllModuleProject(getProject().getRootProject());
        for (Project project : allModuleProject) {
            File modelSrcFile = new File(project.getProject().getProjectDir(), Constants.SOURCE_CODE_DIR);
            if (modelSrcFile.exists()) {
                combinedTypeSolver.add(new JavaParserTypeSolver(modelSrcFile));
            }
        }

        JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        ParserConfiguration parserConfiguration = new ParserConfiguration()
                .setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
                .setSymbolResolver(javaSymbolSolver);
        javaParser = new JavaParser(parserConfiguration);
    }

    /**
     * 获取所有源代码
     *
     * @param sourceCodeDirFile 源代码目录
     * @return 所有源代码
     */
    private List<CompilationUnit> getAllCompilationUnit(File sourceCodeDirFile) throws IOException {
        List<CompilationUnit> compilationUnitList = new ArrayList<>();
        Path path = Paths.get(sourceCodeDirFile.getAbsolutePath());
        SourceRoot sourceRoot = new SourceRoot(path, javaParser.getParserConfiguration());
        List<ParseResult<CompilationUnit>> results = sourceRoot.tryToParse();
        for (ParseResult<CompilationUnit> result : results) {
            if (result.isSuccessful()) {
                result.getResult().ifPresent(compilationUnitList::add);
            }
        }
        return compilationUnitList;
    }

    /**
     * 过滤出控制器
     *
     * @param compilationUnitList 所有源代码
     * @return 控制器
     */
    private List<CompilationUnit> filterController(List<CompilationUnit> compilationUnitList) {
        List<CompilationUnit> controllerCompilationUnitList = new ArrayList<>();
        for (CompilationUnit compilationUnit : compilationUnitList) {
            List<AnnotationExpr> annotationExprList = compilationUnit.findAll(AnnotationExpr.class);
            if (annotationExprList == null) {
                continue;
            }
            for (AnnotationExpr annotationExpr : annotationExprList) {
                if (Objects.equals(annotationExpr.getNameAsString(), "Controller") || Objects.equals(annotationExpr.getNameAsString(), "RestController")) {
                    controllerCompilationUnitList.add(compilationUnit);
                }
            }
        }
        return controllerCompilationUnitList;
    }

    /**
     * 过滤出Graphql控制器方法
     *
     * @param controllerCompilationUnit 控制器
     * @param type                      请求类型
     * @return Graphql控制器方法
     */
    private List<MethodDeclaration> filterGraphqlControllerMethod(CompilationUnit controllerCompilationUnit, String type) {
        List<MethodDeclaration> resultList = new ArrayList<>();
        List<MethodDeclaration> methodDeclarationList = controllerCompilationUnit.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarationList) {
            List<AnnotationExpr> annotationExprList = methodDeclaration.findAll(AnnotationExpr.class);
            for (AnnotationExpr annotationExpr : annotationExprList) {
                if (Objects.equals(annotationExpr.getNameAsString(), type)) {
                    resultList.add(methodDeclaration);
                }
            }
        }
        return resultList;
    }

    /**
     * 获取方法参数
     *
     * @param methodDeclaration 方法
     * @return 方法参数
     */
    private List<Parameter> filterGraphqlArgument(MethodDeclaration methodDeclaration) {
        List<Parameter> methodParameterList = methodDeclaration.findAll(Parameter.class);
        List<Parameter> parameterList = new ArrayList<>();
        for (Parameter parameter : methodParameterList) {
            List<AnnotationExpr> a = parameter.findAll(AnnotationExpr.class);
            for (AnnotationExpr expr : a) {
                if (expr.getNameAsString().equals("Argument")) {
                    parameterList.add(parameter);
                    break;
                }
            }
        }
        return parameterList;
    }
}
