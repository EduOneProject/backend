package io.github.summerain0.plugin.gradle.generator.gqldoc.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 字段类型信息描述模型
 *
 * @author summerain0
 */
@Data
public class FieldTypeInfoModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字段类型名称
     */
    private String typeName;

    /**
     * 指令描述信息
     */
    private Set<String> directiveInfoSet = new HashSet<>();

    /**
     * 创建字段类型信息描述模型
     *
     * @param typeName 字段类型名称
     * @return 字段类型信息描述模型
     */
    public static FieldTypeInfoModel of(String typeName) {
        FieldTypeInfoModel model = new FieldTypeInfoModel();
        model.setTypeName(typeName);
        return model;
    }

    /**
     * 创建字段类型信息描述模型
     *
     * @param typeName  字段类型名称
     * @param directive 指令描述信息
     * @return 字段类型信息描述模型
     */
    public static FieldTypeInfoModel of(String typeName, String directive, Set<String> parentDirectiveInfoSet) {
        FieldTypeInfoModel model = of(typeName);
        HashSet<String> result = new HashSet<>(parentDirectiveInfoSet);
        if (directive != null && !directive.isEmpty()) {
            result.add(directive);
        }
        model.setDirectiveInfoSet(result);
        return model;
    }

    @Override
    public String toString() {
        if (directiveInfoSet.isEmpty()) {
            return typeName;
        } else {
            return typeName + " " + String.join(" ", directiveInfoSet);
        }
    }
}
