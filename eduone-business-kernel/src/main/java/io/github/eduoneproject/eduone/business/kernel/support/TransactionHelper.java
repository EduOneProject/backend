package io.github.eduoneproject.eduone.business.kernel.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 事务辅助类
 *
 * @author summerain0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionHelper {
    private final PlatformTransactionManager platformTransactionManager;
    private final TransactionDefinition transactionDefinition;

    /**
     * 执行事务
     *
     * @param callback 回调
     */
    public void execute(TransactionCallback callback) {
        execute(callback, transactionDefinition);
    }

    /**
     * 执行事务
     *
     * @param callback   回调
     * @param definition 事务定义配置
     */
    public void execute(TransactionCallback callback, TransactionDefinition definition) {
        if (callback == null) {
            return;
        }
        TransactionStatus status = platformTransactionManager.getTransaction(definition);
        try {
            callback.execute();
            if (!status.isCompleted()) {
                platformTransactionManager.commit(status);
            }
        } catch (Throwable t) {
            handleTransactionFailure(status, t, callback);
        }
    }

    /**
     * 执行事务
     *
     * @param callback 回调
     * @param <T>      返回值类型
     * @return 回调执行结果
     */
    public <T> T executeWithResult(TransactionResultCallback<T> callback) {
        return executeWithResult(callback, transactionDefinition);
    }

    /**
     * 执行事务
     *
     * @param callback   回调
     * @param definition 事务定义配置
     * @param <T>        返回值类型
     * @return 回调执行结果
     */
    public <T> T executeWithResult(TransactionResultCallback<T> callback, TransactionDefinition definition) {
        if (callback == null) {
            return null;
        }
        TransactionStatus status = platformTransactionManager.getTransaction(definition);
        T result;
        try {
            result = callback.execute();
            if (!status.isCompleted()) {
                platformTransactionManager.commit(status);
            }
            return result;
        } catch (Throwable t) {
            handleTransactionFailure(status, t, callback);
            throw t;
        }
    }

    /**
     * 处理事务失败
     *
     * @param status   事务状态
     * @param t        异常
     * @param callback 回调
     */
    private void handleTransactionFailure(TransactionStatus status, Throwable t, BaseTransactionCallback callback) {
        if (!status.isCompleted()) {
            try {
                platformTransactionManager.rollback(status);
            } catch (Throwable rollbackEx) {
                log.error("事务回滚本身异常", rollbackEx);
            }
        }
        try {
            callback.rollback(t);
        } catch (Throwable cbEx) {
            log.error("事务回滚回调执行异常", cbEx);
        }
        log.error("事务执行异常", t);
    }

    /**
     * 回调接口分层
     */
    public interface BaseTransactionCallback {
        default void rollback(Throwable t) {
        }
    }

    /**
     * 无返回值的事务回调
     */
    public interface TransactionCallback extends BaseTransactionCallback {
        void execute();
    }

    /**
     * 带返回值的事务回调
     */
    public interface TransactionResultCallback<T> extends BaseTransactionCallback {
        T execute();
    }

    /**
     * 新建事务定义，挂起外层事务，单独开一个全新独立事务
     */
    public TransactionDefinition newRequiresNewDefinition() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return def;
    }

    /**
     * 新建事务定义，只读事务
     */
    public TransactionDefinition newReadOnlyDefinition() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setReadOnly(true);
        return def;
    }

    /**
     * 新建事务定义：有事务就加入外层事务，无事务则非事务执行
     */
    public TransactionDefinition newSupportsDefinition() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        return def;
    }

    /**
     * 新建事务定义：强制外层必须存在事务，不存在则抛异常
     */
    public TransactionDefinition newMandatoryDefinition() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY);
        return def;
    }

    /**
     * 新建事务定义：挂起外层事务，以内层无事务方式执行
     */
    public TransactionDefinition newNotSupportedDefinition() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        return def;
    }

    /**
     * 新建事务定义：禁止外层存在事务，有事务直接抛异常
     */
    public TransactionDefinition newNeverDefinition() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NEVER);
        return def;
    }

    /**
     * 新建事务定义：嵌套事务，外层有事务则创建保存点，外层无事务则新建事务
     */
    public TransactionDefinition newNestedDefinition() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
        return def;
    }

    /**
     * 新建事务定义：默认传播行为 REQUIRED，有事务加入，无事务新建
     */
    public TransactionDefinition newRequiredDefinition() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return def;
    }
}