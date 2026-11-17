package io.github.eduoneproject.eduone.business.kernel.security.model;

import io.github.eduoneproject.eduone.business.kernel.security.consts.PrincipalType;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户身份
 *
 * @author summerain0
 */
@ToString
public final class UserPrincipal implements Principal, Serializable {
    /**
     * 身份唯一标识
     */
    private final PrincipalId id;

    /**
     * 账户身份唯一标识
     */
    private final PrincipalId accountId;

    /**
     * 私有构造函数
     *
     * @param id        身份唯一标识
     * @param accountId 账户身份唯一标识
     */
    private UserPrincipal(PrincipalId id, PrincipalId accountId) {
        this.id = Objects.requireNonNull(id);
        this.accountId = Objects.requireNonNull(accountId);
    }

    /**
     * 根据用户ID创建用户身份
     *
     * @param userId    用户ID
     * @param accountId 账户身份唯一标识
     * @return 用户身份
     */
    public static UserPrincipal of(String userId, String accountId) {
        return new UserPrincipal(PrincipalId.of(userId), PrincipalId.of(accountId));
    }

    @Override
    public PrincipalId getId() {
        return id;
    }

    @Override
    public PrincipalId getAccountId() {
        return this.accountId;
    }

    @Override
    public PrincipalType getType() {
        return PrincipalType.USER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPrincipal that)) return false;
        return id.equals(that.id) && accountId.equals(that.accountId);
    }

    @Override
    public int hashCode() {
        return id.hashCode() ^ accountId.hashCode();
    }
}