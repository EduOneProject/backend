package io.github.eduoneproject.eduone.query.kernel.service.useraccount.assembler;

import io.github.eduoneproject.eduone.query.kernel.repository.useraccount.dto.AccountAuthDto;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.model.AccountAuthModel;

/**
 * 账号认证信息模型组装器
 *
 * @author summerain0
 */
public class AccountAuthAssembler {
    public static AccountAuthModel assemble(AccountAuthDto dto) {
        if (dto == null) {
            return null;
        }
        AccountAuthModel model = new AccountAuthModel();
        model.setAccountId(dto.getAccountId());
        model.setAuthType(dto.getAuthType());
        model.setAuthKey(dto.getAuthKey());
        model.setAuthSecret(dto.getAuthSecret());
        model.setFailCount(dto.getFailCount());
        model.setMaxFailCount(dto.getMaxFailCount());
        model.setLockUntil(dto.getLockUntil());
        model.setLastAuthTime(dto.getLastAuthTime());
        model.setLastAuthIp(dto.getLastAuthIp());
        model.setStatus(dto.getStatus());
        model.setExpireTime(dto.getExpireTime());
        model.setCreateTime(dto.getCreateTime());
        model.setUpdateTime(dto.getUpdateTime());
        return model;
    }
}
