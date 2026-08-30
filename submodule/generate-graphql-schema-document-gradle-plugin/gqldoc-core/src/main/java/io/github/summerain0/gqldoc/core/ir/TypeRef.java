package io.github.summerain0.gqldoc.core.ir;

import io.github.summerain0.gqldoc.core.enums.TypeKind;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 类型引用描述
 *
 * @author summerain0
 */
@Builder
public class TypeRef implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 类型名称
     */
    @Getter
    private final String name;

    /**
     * 类型类别
     */
    @Getter
    private final TypeKind kind;

    /**
     * 内联类型
     */
    @Getter
    private final TypeRef ofType;

    /**
     * 是否非空
     */
    @Getter
    private final boolean nonNullable;

    /**
     * 指令列表
     */
    @Getter
    private final List<DirectiveRef> directives;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeRef that)) return false;
        return nonNullable == that.nonNullable
                && name.equals(that.name)
                && kind == that.kind
                && Objects.equals(ofType, that.ofType)
                && directives.equals(that.directives);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, kind, ofType, nonNullable, directives);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (directives != null) {
            for (DirectiveRef d : directives) {
                sb.append(" ").append(d);
            }
        }
        if (nonNullable) sb.append("!");
        return sb.toString();
    }
}
