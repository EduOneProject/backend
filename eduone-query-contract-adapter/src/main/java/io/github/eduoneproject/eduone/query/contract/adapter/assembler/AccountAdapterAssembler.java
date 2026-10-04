package io.github.eduoneproject.eduone.query.contract.adapter.assembler;

import io.github.eduoneproject.eduone.query.contract.view.AccountView;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.model.AccountModel;

/**
 * 账号信息模型组装器
 *
 * @author summerain0
 */
public class AccountAdapterAssembler {
    public static AccountView assemble(AccountModel model) {
        if (model == null) {
            return null;
        }
        AccountView view = new AccountView();
        view.setId(model.getId());
        view.setUserId(model.getUserId());
        view.setChannel(model.getChannel());
        view.setStatus(model.getStatus());
        view.setExpireTime(model.getExpireTime());
        view.setLockTime(model.getLockTime());
        view.setLockReason(model.getLockReason());
        view.setCreateUserId(model.getCreateUserId());
        view.setCreateTime(model.getCreateTime());
        view.setUpdateUserId(model.getUpdateUserId());
        view.setUpdateTime(model.getUpdateTime());
        return view;
    }
}
