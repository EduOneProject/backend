package io.github.summerain0.gqldoc.parser.java.util;

/**
 * 类工具类
 */
public class ClassUtils {
    /**
     * 获取简单名称
     *
     * @param qualifiedName 全限定名称
     * @return 简单名称
     */
    public static String getSimpleName(String qualifiedName) {
        return qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
    }
}
