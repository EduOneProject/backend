package io.github.eduoneproject.eduone.query.contract.adapter.assembler;

import io.github.eduoneproject.eduone.query.contract.view.AccountAuthView;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.model.AccountAuthModel;

/**
 * 账号认证信息模型组装器
 *
 * @author summerain0
 */
public class AccountAuthAdapterAssembler {
    public static AccountAuthView assemble(AccountAuthModel model) {
        if (model == null) {
            return null;
        }
        AccountAuthView view = new AccountAuthView();
        view.setAccountId(model.getAccountId());
        view.setAuthType(model.getAuthType());
        view.setAuthKey(model.getAuthKey());
        view.setAuthSecret(model.getAuthSecret());
        view.setFailCount(model.getFailCount());
        view.setMaxFailCount(model.getMaxFailCount());
        view.setLockUntil(model.getLockUntil());
        view.setLastAuthTime(model.getLastAuthTime());
        view.setLastAuthIp(model.getLastAuthIp());
        view.setStatus(model.getStatus());
        view.setExpireTime(model.getExpireTime());
        view.setCreateTime(model.getCreateTime());
        view.setUpdateTime(model.getUpdateTime());
        return view;
    }
}
