package io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 业务字典查询条件
 *
 * @author summerain0
 */
@Data
public class BusinessDictionaryRParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典ID集合
     */
    private List<String> idList;

    /**
     * 字典类型
     */
    private String type;

    /**
     * 字典编码集合
     */
    private List<String> codeList;
}
