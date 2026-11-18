package io.github.eduoneproject.eduone.business.kernel.repository;

import io.github.eduoneproject.eduone.business.kernel.repository.model.AuthRefreshTokenModel;
import io.github.eduoneproject.eduone.business.kernel.repository.param.UpsertAuthRefreshTokenParam;

import java.time.OffsetDateTime;

/**
 * 刷新令牌仓储层接口
 *
 * @author summerain0
 */
public interface AuthRefreshTokenRepository {
    /**
     * 保存刷新令牌信息
     *
     * @param param 刷新令牌信息
     * @return 受影响的行数
     */
    int saveRecord(UpsertAuthRefreshTokenParam param);

    /**
     * 更新刷新令牌信息
     *
     * @param param 刷新令牌信息
     * @return 受影响的行数
     */
    int updateRecordById(UpsertAuthRefreshTokenParam param);

    /**
     * 根据刷新令牌哈希更新刷新令牌轮换信息（乐观锁）
     *
     * @param tokenHash  刷新令牌哈希
     * @param used       是否已被轮换使用
     * @param usedTime   被轮换使用的时间
     * @param replacedBy 替换的刷新令牌ID
     * @return 受影响的行数，0表示没有更新，需要判断是否是重放攻击
     */
    int updateUsedByTokenHashIfUnused(String tokenHash, Boolean used, OffsetDateTime usedTime, String replacedBy);

    /**
     * 根据用户ID更新已使用的刷新令牌吊销信息
     *
     * @param userId      用户ID
     * @param revoked     是否已被吊销
     * @param revokedTime 吊销的时间
     * @return 受影响的行数
     */
    int updateRevokedForUsedTokenByUserId(String userId, Boolean revoked, OffsetDateTime revokedTime);

    /**
     * 根据用户ID和刷新令牌哈希获取刷新令牌信息
     *
     * @param tokenHash 刷新令牌哈希
     * @return 刷新令牌信息
     */
    AuthRefreshTokenModel getRecordByTokenHash(String tokenHash);
}
