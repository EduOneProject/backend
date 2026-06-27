package io.github.eduoneproject.eduone.query.kernel.service.region.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 地区树查询参数
 *
 * @author summerain0
 */
@Data
public class RegionTreeKParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 地区ID列表
     */
    private List<String> idList;

    /**
     * 地区编码列表
     */
    private List<String> codeList;

    /**
     * 上级地区id列表
     */
    private List<String> parentIdList;

    /**
     * 上级地区编码列表
     */
    private List<String> parentCodeList;

    /**
     * 地区状态
     */
    private String status;
}
