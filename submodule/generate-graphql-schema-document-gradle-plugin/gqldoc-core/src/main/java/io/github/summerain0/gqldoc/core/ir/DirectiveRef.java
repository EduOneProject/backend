package io.github.summerain0.gqldoc.core.ir;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 指令引用描述
 *
 * @author summerain0
 */
@Builder
public class DirectiveRef implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 指令名称
     */
    @Getter
    private final String name;

    /**
     * 指令参数
     */
    @Getter
    private final Map<String, Object> arguments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectiveRef that)) return false;
        return name.equals(that.name) && arguments.equals(that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, arguments);
    }

    @Override
    public String toString() {
        if (arguments.isEmpty()) {
            return "@" + name;
        }
        String args = arguments.entrySet().stream()
                .map(e -> e.getKey() + ": \"" + e.getValue() + "\"")
                .collect(Collectors.joining(", "));
        return "@" + name + "(" + args + ")";
    }
}
