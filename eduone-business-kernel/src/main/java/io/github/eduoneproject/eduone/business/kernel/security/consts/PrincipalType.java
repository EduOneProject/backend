package io.github.eduoneproject.eduone.business.kernel.security.consts;

/**
 * 身份类型
 *
 * @author summerain0
 */
public enum PrincipalType {
    /**
     * 普通用户
     */
    USER,

    /**
     * 服务账号（内部服务间调用）
     */
    SERVICE,

    /**
     * 系统账号（定时任务、后台作业）
     */
    SYSTEM,

    /**
     * 匿名
     */
    ANONYMOUS
}