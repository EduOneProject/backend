package io.github.summerain0.gqldoc.core.ir;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Schema描述信息
 *
 * @author summerain0
 */
@Builder
public class SchemaIR implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Schema名称
     */
    @Getter
    private final String schemaName;

    /**
     * 查询字段列表
     */
    @Getter
    private final List<OperationField> queryFields;

    /**
     * 变更字段列表
     */
    @Getter
    private final List<OperationField> mutationFields;

    /**
     * 类型定义
     */
    @Getter
    private final Map<String, TypeDef> definitions;
}
