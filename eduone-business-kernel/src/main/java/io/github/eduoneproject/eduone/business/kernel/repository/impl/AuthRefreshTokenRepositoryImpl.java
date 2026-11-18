package io.github.eduoneproject.eduone.business.kernel.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.eduoneproject.eduone.business.kernel.repository.AuthRefreshTokenRepository;
import io.github.eduoneproject.eduone.business.kernel.repository.model.AuthRefreshTokenModel;
import io.github.eduoneproject.eduone.business.kernel.repository.param.UpsertAuthRefreshTokenParam;
import io.github.eduoneproject.eduone.business.kernel.repository.pojo.AuthRefreshTokenPO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.time.OffsetDateTime;

/**
 * 刷新令牌仓储层实现
 *
 * @author summerain0
 */
@Mapper
public interface AuthRefreshTokenRepositoryImpl extends AuthRefreshTokenRepository, BaseMapper<AuthRefreshTokenPO> {
    @Override
    default int saveRecord(UpsertAuthRefreshTokenParam param) {
        AuthRefreshTokenPO po = map(param);
        if (po == null) {
            return 0;
        }
        return insert(po);
    }

    @Override
    default int updateRecordById(UpsertAuthRefreshTokenParam param) {
        AuthRefreshTokenPO po = map(param);
        if (po == null) {
            return 0;
        }
        return updateById(po);
    }

    @Override
    default int updateUsedByTokenHashIfUnused(String tokenHash, Boolean used, OffsetDateTime usedTime, String replacedBy) {
        if (StringUtils.isBlank(tokenHash)) {
            return 0;
        }
        if (used == null) {
            throw new IllegalArgumentException("used cannot be null");
        }
        LambdaUpdateWrapper<AuthRefreshTokenPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AuthRefreshTokenPO::getTokenHash, tokenHash);
        updateWrapper.eq(AuthRefreshTokenPO::getUsed, false);
        updateWrapper.set(AuthRefreshTokenPO::getUsed, used);
        if (usedTime != null) {
            updateWrapper.set(AuthRefreshTokenPO::getUsedTime, usedTime);
        }
        if (replacedBy != null) {
            updateWrapper.set(AuthRefreshTokenPO::getReplacedBy, replacedBy);
        }
        return update(updateWrapper);
    }

    @Override
    default int updateRevokedForUsedTokenByUserId(String userId, Boolean revoked, OffsetDateTime revokedTime) {
        if (StringUtils.isBlank(userId)) {
            return 0;
        }
        if (revoked == null) {
            throw new IllegalArgumentException("revoked cannot be null");
        }
        LambdaUpdateWrapper<AuthRefreshTokenPO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AuthRefreshTokenPO::getUserId, userId);
        updateWrapper.eq(AuthRefreshTokenPO::getUsed, true);
        updateWrapper.set(AuthRefreshTokenPO::getRevoked, revoked);
        if (revokedTime != null) {
            updateWrapper.set(AuthRefreshTokenPO::getRevokedTime, revokedTime);
        }
        return update(updateWrapper);
    }

    @Override
    default AuthRefreshTokenModel getRecordByTokenHash(String tokenHash) {
        if (StringUtils.isBlank(tokenHash)) {
            return null;
        }
        LambdaQueryWrapper<AuthRefreshTokenPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AuthRefreshTokenPO::getTokenHash, tokenHash);
        AuthRefreshTokenPO po = selectOne(queryWrapper);
        return map(po);
    }

    private AuthRefreshTokenPO map(UpsertAuthRefreshTokenParam param) {
        if (param == null) {
            return null;
        }
        AuthRefreshTokenPO po = new AuthRefreshTokenPO();
        po.setId(param.getId());
        po.setTokenHash(param.getTokenHash());
        po.setSessionId(param.getSessionId());
        po.setAccountId(param.getAccountId());
        po.setUserId(param.getUserId());
        po.setCreatedTime(param.getCreatedTime());
        po.setExpiresTime(param.getExpiresTime());
        po.setUsed(param.getUsed());
        po.setUsedTime(param.getUsedTime());
        po.setRevoked(param.getRevoked());
        po.setRevokedTime(param.getRevokedTime());
        po.setReplacedBy(param.getReplacedBy());
        po.setDeviceId(param.getDeviceId());
        po.setUserAgent(param.getUserAgent());
        po.setIpAddress(param.getIpAddress());
        return po;
    }

    private AuthRefreshTokenModel map(AuthRefreshTokenPO po) {
        if (po == null) {
            return null;
        }
        AuthRefreshTokenModel model = new AuthRefreshTokenModel();
        model.setId(po.getId());
        model.setTokenHash(po.getTokenHash());
        model.setSessionId(po.getSessionId());
        model.setAccountId(po.getAccountId());
        model.setUserId(po.getUserId());
        model.setCreatedTime(po.getCreatedTime());
        model.setExpiresTime(po.getExpiresTime());
        model.setUsed(po.getUsed());
        model.setUsedTime(po.getUsedTime());
        model.setRevoked(po.getRevoked());
        model.setRevokedTime(po.getRevokedTime());
        model.setReplacedBy(po.getReplacedBy());
        model.setDeviceId(po.getDeviceId());
        model.setUserAgent(po.getUserAgent());
        model.setIpAddress(po.getIpAddress());
        return model;
    }
}
