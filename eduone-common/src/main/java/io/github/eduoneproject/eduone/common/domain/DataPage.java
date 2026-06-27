package io.github.eduoneproject.eduone.common.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页信息
 *
 * @author summerain0
 */
@Data
public class DataPage<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页数量
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 当前页
     */
    private long pageNo;

    /**
     * 每页数据数量
     */
    private long pageSize;

    /**
     * 当前页数据信息
     */
    private List<T> currentPageData;

    /**
     * 总数量
     */
    private long totalSize;

    /**
     * 构造方法
     */
    public DataPage() {
        this.pageNo = DEFAULT_PAGE_NUM;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    /**
     * 构造方法
     *
     * @param pageNo   页码
     * @param pageSize 每页数量
     */
    public DataPage(long pageNo, long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public long getTotalPage() {
        if (this.totalSize == 0) {
            return 0;
        } else {
            return (long) Math.ceil((double) totalSize / pageSize);
        }
    }
}
