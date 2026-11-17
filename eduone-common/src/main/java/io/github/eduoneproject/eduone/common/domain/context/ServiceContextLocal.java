package io.github.eduoneproject.eduone.common.domain.context;

/**
 * 当前线程服务上下文
 *
 * @author summerain0
 */
public class ServiceContextLocal {
    private static final ThreadLocal<ServiceContext> SERVICE_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private ServiceContextLocal() {
    }

    /**
     * 设置当前线程的上下文信息
     *
     * @param serviceContext 服务上下文信息
     */
    public static void setServiceContext(ServiceContext serviceContext) {
        SERVICE_CONTEXT_THREAD_LOCAL.set(serviceContext);
    }

    /**
     * 移除当前线程的上下文信息
     */
    public static void removeServiceContext() {
        SERVICE_CONTEXT_THREAD_LOCAL.remove();
    }

    /**
     * 获取当前线程上下文信息
     *
     * @return 服务上下文信息
     */
    public static ServiceContext getServiceContext() {
        return SERVICE_CONTEXT_THREAD_LOCAL.get();
    }
}
