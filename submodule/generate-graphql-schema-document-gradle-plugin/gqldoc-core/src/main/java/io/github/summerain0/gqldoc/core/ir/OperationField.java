package io.github.summerain0.gqldoc.core.ir;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 操作字段描述
 *
 * @author summerain0
 */
@Builder
public class OperationField implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @Getter
    private final String name;

    /**
     * 描述
     */
    @Getter
    private final String description;

    /**
     * 参数列表
     */
    @Getter
    private final List<ArgumentDef> arguments;

    /**
     * 返回类型
     */
    @Getter
    private final TypeRef returnType;

    /**
     * 是否弃用
     */
    @Getter
    private final boolean deprecated;

    /**
     * 弃用原因
     */
    @Getter
    private final String deprecationReason;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationField that)) return false;
        return name.equals(that.name)
                && returnType.equals(that.returnType)
                && arguments.equals(that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, arguments);
    }
}
