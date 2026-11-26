package io.github.eduoneproject.eduone.repository.domain.usersession.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.eduoneproject.eduone.repository.domain.usersession.pojo.UserSessionPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户会话仓储层
 *
 * @author summerain0
 */
@Mapper
public interface UserSessionRepository extends BaseMapper<UserSessionPO> {
}
