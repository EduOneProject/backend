package io.github.summerain0.gqldoc.core.ir;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 指令定义描述
 *
 * @author summerain0
 */
@Builder
public class DirectiveDef implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 指令名称
     */
    @Getter
    private final String name;

    /**
     * 参数列表
     */
    @Getter
    private final List<ArgumentDef> arguments;

    /**
     * 可应用位置列表
     */
    @Getter
    private final List<String> locations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectiveDef that)) return false;
        return name.equals(that.name)
                && Objects.equals(arguments, that.arguments)
                && Objects.equals(locations, that.locations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, arguments, locations);
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
