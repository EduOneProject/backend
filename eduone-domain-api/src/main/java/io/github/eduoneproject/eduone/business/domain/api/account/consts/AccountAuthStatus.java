package io.github.eduoneproject.eduone.business.domain.api.account.consts;

import lombok.Getter;

/**
 * 账户认证信息状态
 *
 * @author summerain0
 */
public enum AccountAuthStatus {
    ACTIVE("ACTIVE", "正常"),
    INACTIVE("INACTIVE", "禁用");

    /**
     * 状态码
     */
    @Getter
    private final String code;

    /**
     * 状态描述
     */
    @Getter
    private final String description;

    /**
     * 枚举构造函数
     *
     * @param code        状态码
     * @param description 状态描述
     */
    AccountAuthStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
