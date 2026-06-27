package io.github.summerain0.plugin.gradle.generator.gqldoc.model.nested;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * GraphQL字段信息
 *
 * @author summerain0
 */
@Data
public class GraphqlFieldInfoModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 参数名称
     */
    private String parameterName;

    /**
     * 类型注释
     */
    private String comment;
}
