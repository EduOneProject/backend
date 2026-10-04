package io.github.eduoneproject.eduone.business.kernel.security.model;

import io.github.eduoneproject.eduone.business.kernel.security.consts.PrincipalType;

/**
 * 身份标识
 *
 * @author summerain0
 */
public interface Principal {
    /**
     * 获取身份唯一标识
     */
    PrincipalId getId();

    /**
     * 获取身份类型
     */
    PrincipalType getType();
}