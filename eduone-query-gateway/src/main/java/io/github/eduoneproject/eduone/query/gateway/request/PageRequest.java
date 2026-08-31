package io.github.eduoneproject.eduone.query.gateway.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页请求条件
 *
 * @author summerain0
 */
@Data
public class PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Long pageNo = 1L;

    /**
     * 每页数量
     */
    private Long pageSize = 10L;
}
