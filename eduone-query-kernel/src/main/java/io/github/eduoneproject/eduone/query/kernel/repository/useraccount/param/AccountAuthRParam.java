package io.github.eduoneproject.eduone.query.kernel.repository.useraccount.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 账号认证信息查询条件
 *
 * @author summerain0
 */
@Data
public class AccountAuthRParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账号ID列表
     */
    private List<String> accountIdList;

    /**
     * 认证类型
     */
    private String authType;

    /**
     * 认证标识
     */
    private String authKey;

    /**
     * 状态
     */
    private String status;
}
