package io.github.eduoneproject.eduone.repository.domain.useraccount.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.eduoneproject.eduone.repository.domain.useraccount.pojo.UserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 *
 * @author summerain0
 */
@Mapper
public interface UserRepository extends BaseMapper<UserPO> {
}
