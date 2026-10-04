package io.github.eduoneproject.eduone.repository.domain.useraccount.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.eduoneproject.eduone.repository.domain.useraccount.pojo.PersonPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人员数据访问层
 *
 * @author summerain0
 */
@Mapper
public interface PersonRepository extends BaseMapper<PersonPO> {
}
