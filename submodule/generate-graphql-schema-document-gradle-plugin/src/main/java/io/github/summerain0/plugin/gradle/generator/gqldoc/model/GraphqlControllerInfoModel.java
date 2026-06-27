package io.github.summerain0.plugin.gradle.generator.gqldoc.model;

import io.github.summerain0.plugin.gradle.generator.gqldoc.model.nested.GraphqlControllerMethodInfoModel;
import lombok.Data;

import java.util.List;

/**
 * gql控制器信息模型
 *
 * @author summerain0
 */
@Data
public class GraphqlControllerInfoModel {
    /**
     * 控制器类名
     */
    private String className;

    /**
     * 方法信息模型
     */
    private List<GraphqlControllerMethodInfoModel> methodInfoModelList;
}
