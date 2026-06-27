package io.github.eduoneproject.eduone.query.gateway.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 地区树查询响应
 *
 * @author summerain0
 */
@Data
public class RegionTreeResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 地区ID
     */
    private String id;

    /**
     * 地区编码
     */
    private String code;

    /**
     * 上级地区ID
     */
    private String parentId;

    /**
     * 上级地区编码
     */
    private String parentCode;

    /**
     * 地区路径
     */
    private String path;

    /**
     * 地区层级
     */
    private Integer level;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 地区排序
     */
    private Integer sort;

    /**
     * 地区状态
     */
    private String status;
}
