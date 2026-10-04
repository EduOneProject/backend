package io.github.eduoneproject.eduone.repository.domain.useraccount.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.eduoneproject.eduone.repository.domain.useraccount.pojo.AccountAuthPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 账号认证信息数据访问层
 *
 * @author summerain0
 */
@Mapper
public interface AccountAuthRepository extends BaseMapper<AccountAuthPO> {
}
