package io.github.eduoneproject.eduone.query.kernel.service.useraccount;

import io.github.eduoneproject.eduone.query.kernel.service.useraccount.model.AccountAuthModel;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.model.AccountModel;

/**
 * 用户账户信息查询接口服务
 *
 * @author summerain0
 */
public interface UserAccountQueryService {
    /**
     * 根据认证标识获取账户认证信息
     *
     * @param authKey 认证标识
     * @return 账户认证信息
     */
    AccountAuthModel getUniqueAccountAuthInfo(String authKey);

    /**
     * 根据账户ID获取账户信息
     *
     * @param accountId 账户ID
     * @return 账户信息
     */
    AccountModel getAccountInfo(String accountId);
}
