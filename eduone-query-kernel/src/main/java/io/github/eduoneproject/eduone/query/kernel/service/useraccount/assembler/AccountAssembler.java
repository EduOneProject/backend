package io.github.eduoneproject.eduone.query.kernel.service.useraccount.assembler;

import io.github.eduoneproject.eduone.query.kernel.repository.useraccount.dto.AccountDto;
import io.github.eduoneproject.eduone.query.kernel.service.useraccount.model.AccountModel;

/**
 * 账号信息模型组装器
 *
 * @author summerain0
 */
public class AccountAssembler {
    public static AccountModel assemble(AccountDto dto) {
        if (dto == null) {
            return null;
        }
        AccountModel model = new AccountModel();
        model.setId(dto.getId());
        model.setUserId(dto.getUserId());
        model.setChannel(dto.getChannel());
        model.setStatus(dto.getStatus());
        model.setExpireTime(dto.getExpireTime());
        model.setLockTime(dto.getLockTime());
        model.setLockReason(dto.getLockReason());
        model.setCreateUserId(dto.getCreateUserId());
        model.setCreateTime(dto.getCreateTime());
        model.setUpdateUserId(dto.getUpdateUserId());
        model.setUpdateTime(dto.getUpdateTime());
        return model;
    }
}
