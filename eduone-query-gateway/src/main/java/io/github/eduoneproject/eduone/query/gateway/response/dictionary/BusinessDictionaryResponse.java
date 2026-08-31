package io.github.eduoneproject.eduone.query.gateway.response.dictionary;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 业务字典信息响应体
 *
 * @author summerain0
 */
@Data
public class BusinessDictionaryResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典ID
     */
    private String id;

    /**
     * 字典类型
     */
    private String type;

    /**
     * 字典编码
     */
    private String code;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典状态
     */
    private String status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 更新人用户ID
     */
    private String updateUserId;

    /**
     * 更新时间
     */
    private OffsetDateTime updateTime;

    /**
     * 创建人用户ID
     */
    private String createUserId;

    /**
     * 创建时间
     */
    private OffsetDateTime createTime;
}
