package io.github.summerain0.gqldoc.core.enums;

/**
 * GraphQL类型分类
 *
 * @author summerain0
 */
public enum TypeKind {
    /**
     * 标量: String, Int, Boolean, Float, DateTime, ID
     */
    SCALAR,

    /**
     * 对象类型: type Xxx { ... }
     */
    OBJECT,

    /**
     * 输入类型: input Xxx { ... }
     */
    INPUT,

    /**
     * 枚举类型: enum Xxx { ... }
     */
    ENUM,

    /**
     * 集合类型: [T] or [T!] or [T]!
     */
    LIST
}