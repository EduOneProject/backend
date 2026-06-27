package io.github.eduoneproject.eduone.query.kernel.repository.region.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 地区信息dto
 *
 * @author summerain0
 */
@Data
public class RegionTreeDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 地区编码
     */
    private String code;

    /**
     * 上级地区id
     */
    private String parentId;

    /**
     * 上级地区编码
     */
    private String parentCode;

    /**
     * 地区层级
     */
    private Integer level;

    /**
     * 地区全路径
     */
    private String path;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态
     */
    private String status;
}
