package io.github.eduoneproject.eduone.repository.domain.role.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.eduoneproject.eduone.repository.domain.role.pojo.RolePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色数据访问层
 *
 * @author summerain0
 */
@Mapper
public interface RoleRepository extends BaseMapper<RolePO> {
}
