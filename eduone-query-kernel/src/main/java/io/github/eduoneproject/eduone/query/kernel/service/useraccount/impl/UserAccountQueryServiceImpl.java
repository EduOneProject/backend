package io.github.eduoneproject.eduone.query.kernel.service.useraccount.impl;

import io.github.eduoneproject.eduone.query.kernel.repository.useraccount.UserAccountQueryRepository;
import io.github.eduoneproject.eduone.query.kernel.repository.useraccount.dto.AccountAuthDto;
import io.github.eduoneproject.eduone.query.kernel.repository.useraccount.dto.AccountDto;
import io.github.eduoneproject.eduone.query.kernel.repository.useraccount.param.AccountAuthRParam;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.UserAccountQueryService;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.assembler.AccountAssembler;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.assembler.AccountAuthAssembler;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.model.AccountAuthModel;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.model.AccountModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户账户信息查询接口服务实现类
 *
 * @author summerain0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserAccountQueryServiceImpl implements UserAccountQueryService {
    private final UserAccountQueryRepository userAccountQueryRepository;

    @Override
    public AccountAuthModel getUniqueAccountAuthInfo(String authKey) {
        if (StringUtils.isBlank(authKey)) {
            return null;
        }
        AccountAuthRParam accountAuthRParam = new AccountAuthRParam();
        accountAuthRParam.setAuthKey(authKey);
        List<AccountAuthDto> dtoList = userAccountQueryRepository.listAccountAuth(accountAuthRParam);
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        if (dtoList.size() > 1) {
            log.error("账户认证信息重复，authKey：{}", authKey);
            throw new RuntimeException("账户认证信息重复");
        }
        AccountAuthDto dto = dtoList.get(0);
        return AccountAuthAssembler.assemble(dto);
    }

    @Override
    public AccountModel getAccountInfo(String accountId) {
        if (StringUtils.isBlank(accountId)) {
            return null;
        }
        AccountDto accountDto = userAccountQueryRepository.getAccountInfoById(accountId);
        return AccountAssembler.assemble(accountDto);
    }
}
