package io.github.summerain0.plugin.gradle.generator.gqldoc.model.nested;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;
import lombok.Data;

import java.util.List;

/**
 * gql控制器方法信息模型
 *
 * @author summerain0
 */
@Data
public class GraphqlControllerMethodInfoModel {
    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法声明
     */
    private MethodDeclaration methodDeclaration;

    /**
     * 方法参数列表
     */
    private List<Parameter> parameterList;

    /**
     * 方法返回值类型
     */
    private Type methodReturnType;

    /**
     * 是否是查询方法
     */
    private boolean queryMethod;
}
