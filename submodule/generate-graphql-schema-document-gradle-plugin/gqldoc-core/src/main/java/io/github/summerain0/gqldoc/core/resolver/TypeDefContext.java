package io.github.summerain0.gqldoc.core.resolver;

import io.github.summerain0.gqldoc.core.enums.TypeKind;
import io.github.summerain0.gqldoc.core.ir.FieldDef;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 类型定义模型上下文信息
 *
 * @author summerain0
 */
@Data
public class TypeDefContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 类型种类
     */
    private TypeKind typeKind;

    /**
     * 描述
     */
    private String description;

    /**
     * 字段定义列表
     */
    private List<FieldDef> fieldDefList;

    /**
     * 是否弃用
     */
    private boolean deprecated;

    /**
     * 弃用原因
     */
    private String deprecationReason;
}
