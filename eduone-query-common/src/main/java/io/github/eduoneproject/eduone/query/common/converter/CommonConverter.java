package io.github.eduoneproject.eduone.query.common.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.eduoneproject.eduone.common.domain.DataPage;
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

    /**
     * 分页集合转换
     *
     * @param sourcePage 源分页集合
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 目标分页集合
     */
    public static <S, T> DataPage<T> convert(DataPage<S> sourcePage) {
        return convert(sourcePage, null);
    }

    /**
     * 分页集合转换
     *
     * @param sourcePage 源分页集合
     * @param func       转换函数
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 目标分页集合
     */
    public static <S, T> DataPage<T> convert(DataPage<S> sourcePage, Function<S, T> func) {
        if (sourcePage == null) {
            return null;
        }
        DataPage<T> targetPage = new DataPage<>();
        targetPage.setPageNo(sourcePage.getPageNo());
        targetPage.setPageSize(sourcePage.getPageSize());
        targetPage.setTotalSize(sourcePage.getTotalSize());
        if (func != null) {
            targetPage.setCurrentPageData(convert(sourcePage.getCurrentPageData(), func));
        }
        return targetPage;
    }

    /**
     * 分页集合转换
     *
     * @param sourcePage 源分页集合
     * @param func       转换函数
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 目标分页集合
     */
    public static <S, T> DataPage<T> convert(IPage<S> sourcePage, Function<S, T> func) {
        if (sourcePage == null) {
            return null;
        }
        DataPage<T> targetPage = new DataPage<>();
        targetPage.setPageNo(sourcePage.getCurrent());
        targetPage.setPageSize(sourcePage.getSize());
        targetPage.setTotalSize(sourcePage.getTotal());
        if (func != null) {
            targetPage.setCurrentPageData(convert(sourcePage.getRecords(), func));
        }
        return targetPage;
    }

    /**
     * 分页集合转换
     *
     * @param sourcePage 源分页集合
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 目标分页集合
     */
    public static <S, T> IPage<T> convert2IPage(DataPage<S> sourcePage) {
        return convert2IPage(sourcePage, null);
    }

    /**
     * 分页集合转换
     *
     * @param sourcePage 源分页集合
     * @param func       转换函数
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 目标分页集合
     */
    public static <S, T> IPage<T> convert2IPage(DataPage<S> sourcePage, Function<S, T> func) {
        if (sourcePage == null) {
            return null;
        }
        IPage<T> targetPage = new Page<>();
        targetPage.setCurrent(sourcePage.getPageNo());
        targetPage.setSize(sourcePage.getPageSize());
        targetPage.setTotal(sourcePage.getTotalSize());
        if (func != null) {
            targetPage.setRecords(convert(sourcePage.getCurrentPageData(), func));
        }
        return targetPage;
    }
}
