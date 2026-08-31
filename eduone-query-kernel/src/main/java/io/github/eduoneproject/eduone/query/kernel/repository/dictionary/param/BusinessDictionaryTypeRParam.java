package io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 业务字典类型查询条件
 *
 * @author summerain0
 */
@Data
public class BusinessDictionaryTypeRParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典类型名称
     */
    private String name;
}
