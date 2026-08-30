package io.github.summerain0.gqldoc.core.ir;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 字段定义描述
 *
 * @author summerain0
 */
@Builder
public class FieldDef implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字段名称
     */
    @Getter
    private final String name;

    /**
     * 字段描述
     */
    @Getter
    private final String description;

    /**
     * 字段类型
     */
    @Getter
    private final TypeRef type;

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
        if (!(o instanceof FieldDef that)) return false;
        return deprecated == that.deprecated
                && name.equals(that.name)
                && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, deprecated);
    }
}
