package io.github.summerain0.plugin.gradle.generator.gqldoc.model;

import io.github.summerain0.plugin.gradle.generator.gqldoc.model.nested.GraphqlFieldInfoModel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * GraphQL输入模型信息
 *
 * @author summerain0
 */
@Data
public class GraphqlInputTypeInfoModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 全限定名称
     */
    private String qualifiedName;

    /**
     * 类型注释
     */
    private String comment;

    /**
     * 是否是参数
     */
    private boolean parameter;

    /**
     * 字段信息
     */
    private List<GraphqlFieldInfoModel> fieldInfoModelList;
}
