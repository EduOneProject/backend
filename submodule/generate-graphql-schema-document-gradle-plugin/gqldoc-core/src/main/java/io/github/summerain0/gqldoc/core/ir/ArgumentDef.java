package io.github.summerain0.gqldoc.core.ir;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 参数定义描述
 *
 * @author summerain0
 */
@Builder
public class ArgumentDef implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 参数名称
     */
    @Getter
    private final String name;

    /**
     * 参数类型
     */
    @Getter
    private final TypeRef type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArgumentDef that)) return false;
        return name.equals(that.name)
                && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return name + ": " + type.toString();
    }
}
