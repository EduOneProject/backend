package io.github.eduoneproject.eduone.query.gateway.response.dictionary;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 业务字典类型信息响应体
 *
 * @author summerain0
 */
@Data
public class BusinessDictionaryTypeResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典类型
     */
    private String type;

    /**
     * 字典类型名称
     */
    private String name;

    /**
     * 字典类型描述
     */
    private String desc;

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
