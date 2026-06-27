package io.github.eduoneproject.eduone.query.common.converter;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 通用转换器
 *
 * @author summerain0
 */
public class CommonConverter {
    /**
     * 集合转换
     *
     * @param sourceList 源集合
     * @param func       转换函数
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 目标集合
     */
    public static <S, T> List<T> convert(List<S> sourceList, Function<S, T> func) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        return sourceList.stream().map(func).toList();
    }
}
