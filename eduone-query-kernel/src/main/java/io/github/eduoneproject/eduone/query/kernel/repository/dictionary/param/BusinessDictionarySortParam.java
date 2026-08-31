package io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param;

import io.github.eduoneproject.eduone.query.common.domain.enums.OrderDirection;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.enums.BusinessDictionarySortField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 业务字典排序条件
 *
 * @author summerain0
 */
@Data
public class BusinessDictionarySortParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 排序字段
     */
    private BusinessDictionarySortField field;

    /**
     * 排序方向
     */
    private OrderDirection direction = OrderDirection.ASC;
}
