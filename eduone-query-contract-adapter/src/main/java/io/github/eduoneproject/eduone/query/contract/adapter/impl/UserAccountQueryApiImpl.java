package io.github.eduoneproject.eduone.query.contract.adapter.impl;

import io.github.eduoneproject.eduone.query.contract.UserAccountQueryApi;
import io.github.eduoneproject.eduone.query.contract.adapter.assembler.AccountAdapterAssembler;
import io.github.eduoneproject.eduone.query.contract.adapter.assembler.AccountAuthAdapterAssembler;
import io.github.eduoneproject.eduone.query.contract.view.AccountAuthView;
import io.github.eduoneproject.eduone.query.contract.view.AccountView;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.UserAccountQueryService;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.model.AccountAuthModel;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.model.AccountModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 用户账户信息接口实现类
 *
 * @author summerain0
 */
@RequiredArgsConstructor
@Service
public class UserAccountQueryApiImpl implements UserAccountQueryApi {
    private final UserAccountQueryService userAccountQueryService;

    @Override
    public AccountAuthView getUniqueAccountAuthInfo(String authKey) {
        if (StringUtils.isBlank(authKey)) {
            return null;
        }
        AccountAuthModel model = userAccountQueryService.getUniqueAccountAuthInfo(authKey);
        return AccountAuthAdapterAssembler.assemble(model);
    }

    @Override
    public AccountView getAccountInfoById(String accountId) {
        if (StringUtils.isBlank(accountId)) {
            return null;
        }
        AccountModel model = userAccountQueryService.getAccountInfo(accountId);
        return AccountAdapterAssembler.assemble(model);
    }
}
