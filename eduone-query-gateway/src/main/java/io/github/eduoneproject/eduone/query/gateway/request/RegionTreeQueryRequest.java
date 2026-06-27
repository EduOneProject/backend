package io.github.eduoneproject.eduone.query.gateway.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 地区树查询条件
 *
 * @author summerain0
 */
@Data
public class RegionTreeQueryRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 上级地区ID，-1表示顶级
     */
    private List<String> parentIdList;

    /**
     * 上级地区编码，-1表示顶级
     */
    private List<String> parentCodeList;

    /**
     * 地区ID，-1表示顶级
     */
    private List<String> idList;

    /**
     * 地区编码，-1表示顶级
     */
    private List<String> codeList;

    /**
     * 地区状态
     */
    private String status;
}
