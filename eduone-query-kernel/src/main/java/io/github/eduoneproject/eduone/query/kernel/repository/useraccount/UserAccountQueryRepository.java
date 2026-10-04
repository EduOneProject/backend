package io.github.eduoneproject.eduone.query.kernel.repository.useraccount;

import io.github.eduoneproject.eduone.query.kernel.repository.useraccount.dto.AccountAuthDto;
import io.github.eduoneproject.eduone.query.kernel.repository.useraccount.dto.AccountDto;
import io.github.eduoneproject.eduone.query.kernel.repository.useraccount.param.AccountAuthRParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户账号信息查询数据访问层
 *
 * @author summerain0
 */
@Mapper
public interface UserAccountQueryRepository {
    /**
     * 根据条件查询账户授权信息
     *
     * @param param 查询条件
     * @return 账户授权信息列表
     */
    List<AccountAuthDto> listAccountAuth(AccountAuthRParam param);

    /**
     * 根据ID查询账户信息
     *
     * @param id 账户ID
     * @return 账户信息
     */
    AccountDto getAccountInfoById(String id);
}