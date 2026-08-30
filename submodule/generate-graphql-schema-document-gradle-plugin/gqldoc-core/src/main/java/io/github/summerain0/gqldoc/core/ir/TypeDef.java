package io.github.summerain0.gqldoc.core.ir;

import io.github.summerain0.gqldoc.core.enums.TypeKind;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 类型定义描述
 *
 * @author summerain0
 */
@Builder
public final class TypeDef implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 类型名称
     */
    @Getter
    private final String name;

    /**
     * 类型描述
     */
    @Getter
    private final String description;

    /**
     * 类型类别
     */
    @Getter
    private final TypeKind kind;

    /**
     * 字段列表
     */
    @Getter
    private final List<FieldDef> fields;

    /**
     * 指令列表
     */
    @Getter
    private final List<DirectiveRef> directives;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeDef that)) return false;
        return name.equals(that.name) && kind == that.kind && fields.equals(that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, kind, fields);
    }

    @Override
    public String toString() {
        return "[" + kind + " " + name + "]";
    }
}
