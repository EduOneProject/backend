package io.github.eduoneproject.eduone.business.kernel.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.eduoneproject.eduone.business.kernel.repository.pojo.AuthRefreshTokenPO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * 刷新令牌数据访问层
 *
 * @author summerain0
 */
@Mapper
public interface AuthRefreshTokenRepository extends BaseMapper<AuthRefreshTokenPO> {
    /**
     * 根据用户ID删除刷新令牌
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    default int deleteByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return 0;
        }
        LambdaQueryWrapper<AuthRefreshTokenPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuthRefreshTokenPO::getUserId, userId);
        return delete(wrapper);
    }
}
