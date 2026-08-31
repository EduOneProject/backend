package io.github.eduoneproject.eduone.query.gateway.request.dictionary;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 业务字典类型查询条件请求体
 *
 * @author summerain0
 */
@Data
public class BusinessDictionaryTypeRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典类型名称
     */
    private String name;
}
