package io.github.eduoneproject.eduone.query.contract;

import io.github.eduoneproject.eduone.query.contract.view.AccountAuthView;
import io.github.eduoneproject.eduone.query.contract.view.AccountView;

/**
 * 用户账户信息查询接口
 *
 * @author summerain0
 */
public interface UserAccountQueryApi {
    /**
     * 根据认证标识获取唯一账户认证信息
     *
     * @param authKey 认证标识
     * @return 账户认证信息
     */
    AccountAuthView getUniqueAccountAuthInfo(String authKey);

    /**
     * 根据账户ID获取账户信息
     *
     * @param accountId 账户ID
     * @return 账户信息
     */
    AccountView getAccountInfoById(String accountId);
}
