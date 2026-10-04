package io.github.eduoneproject.eduone.repository.domain.useraccount.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.eduoneproject.eduone.repository.domain.useraccount.pojo.AccountRolePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 账号角色信息数据访问层
 *
 * @author summerain0
 */
@Mapper
public interface AccountRoleRepository extends BaseMapper<AccountRolePO> {
}
