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
     * 私有构造函数
     *
     * @param id 身份唯一标识
     */
    private UserPrincipal(PrincipalId id) {
        this.id = Objects.requireNonNull(id);
    }

    /**
     * 根据用户ID创建用户身份
     *
     * @param userId 用户ID
     * @return 用户身份
     */
    public static UserPrincipal of(String userId) {
        return new UserPrincipal(PrincipalId.of(userId));
    }

    @Override
    public PrincipalId getId() {
        return id;
    }

    @Override
    public PrincipalType getType() {
        return PrincipalType.USER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPrincipal that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}