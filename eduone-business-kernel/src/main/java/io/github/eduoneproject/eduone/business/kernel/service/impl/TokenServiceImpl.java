package io.github.eduoneproject.eduone.business.kernel.service.impl;

import io.github.eduoneproject.eduone.business.common.config.ExpirationConfig;
import io.github.eduoneproject.eduone.business.kernel.repository.AuthRefreshTokenRepository;
import io.github.eduoneproject.eduone.business.kernel.repository.model.AuthRefreshTokenModel;
import io.github.eduoneproject.eduone.business.kernel.repository.param.UpsertAuthRefreshTokenParam;
import io.github.eduoneproject.eduone.business.kernel.service.TokenService;
import io.github.eduoneproject.eduone.business.kernel.service.model.AccessTokenJwtPayload;
import io.github.eduoneproject.eduone.business.kernel.service.model.RefreshToken;
import io.github.eduoneproject.eduone.business.kernel.service.model.RotateAccessTokenResult;
import io.github.eduoneproject.eduone.business.kernel.service.model.UserTokenResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateAccessTokenParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateRefreshTokenParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateUserTokenParam;
import io.github.eduoneproject.eduone.business.kernel.support.TransactionHelper;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import io.github.eduoneproject.eduone.common.util.IdUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Token业务接口实现类
 *
 * @author summerain0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private static final String CLAIMS_KEY_SESSION_ID = "sid";
    private static final String CLAIMS_KEY_ACCOUNT_ID = "aid";
    private static final String CLAIMS_KEY_USER_ID = "uid";

    @Qualifier("accessTokenSecretKey")
    private final SecretKey accessTokenSecretKey;
    private final ExpirationConfig expirationConfig;
    private final AuthRefreshTokenRepository authRefreshTokenRepository;
    private final TransactionHelper transactionHelper;

    @Override
    public UserTokenResult generateUserToken(CreateUserTokenParam param) {
        param.validate();

        // 生成访问令牌
        CreateAccessTokenParam createAccessTokenParam = new CreateAccessTokenParam();
        createAccessTokenParam.setSessionId(param.getSessionId());
        createAccessTokenParam.setAccountId(param.getAccountId());
        createAccessTokenParam.setUserId(param.getUserId());
        createAccessTokenParam.setIssuedAt(param.getIssuedAt());
        createAccessTokenParam.setExpirationTime(param.getAccessTokenExpirationTime());
        String accessToken = createAccessToken(createAccessTokenParam);

        // 生成刷新令牌
        CreateRefreshTokenParam createRefreshTokenParam = new CreateRefreshTokenParam();
        createRefreshTokenParam.setSessionId(param.getSessionId());
        createRefreshTokenParam.setAccountId(param.getAccountId());
        createRefreshTokenParam.setUserId(param.getUserId());
        createRefreshTokenParam.setIssuedAt(param.getIssuedAt());
        createRefreshTokenParam.setExpirationTime(param.getRefreshTokenExpirationTime());
        createRefreshTokenParam.setDeviceId(param.getDeviceId());
        createRefreshTokenParam.setIpAddress(param.getIpAddress());
        createRefreshTokenParam.setUserAgent(param.getUserAgent());
        String refreshToken = createRefreshToken(createRefreshTokenParam);

        UserTokenResult result = new UserTokenResult();
        result.setUserId(param.getUserId());
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);
        return result;
    }

    @Override
    public String createAccessToken(CreateAccessTokenParam param) {
        param.validate();
        return Jwts.builder()
                .id(IdUtils.uuid())
                .subject(param.getAccountId())
                .claim(CLAIMS_KEY_SESSION_ID, param.getSessionId())
                .claim(CLAIMS_KEY_ACCOUNT_ID, param.getAccountId())
                .claim(CLAIMS_KEY_USER_ID, param.getUserId())
                .issuedAt(Date.from(param.getIssuedAt().toInstant()))
                .expiration(Date.from(param.getExpirationTime().toInstant()))
                .signWith(accessTokenSecretKey)
                .compact();
    }

    @Override
    public String createRefreshToken(CreateRefreshTokenParam param) {
        param.validate();

        String refreshTokenRaw = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.of(refreshTokenRaw);

        UpsertAuthRefreshTokenParam upsertAuthRefreshTokenParam = new UpsertAuthRefreshTokenParam();
        upsertAuthRefreshTokenParam.setId(IdUtils.simpleUuid());
        upsertAuthRefreshTokenParam.setTokenHash(refreshToken.getHash());
        upsertAuthRefreshTokenParam.setSessionId(param.getSessionId());
        upsertAuthRefreshTokenParam.setAccountId(param.getAccountId());
        upsertAuthRefreshTokenParam.setUserId(param.getUserId());
        upsertAuthRefreshTokenParam.setExpiresTime(param.getExpirationTime());
        upsertAuthRefreshTokenParam.setUsed(false);
        upsertAuthRefreshTokenParam.setCreatedTime(param.getIssuedAt());
        upsertAuthRefreshTokenParam.setRevoked(false);
        upsertAuthRefreshTokenParam.setDeviceId(param.getDeviceId());
        upsertAuthRefreshTokenParam.setUserAgent(param.getUserAgent());
        upsertAuthRefreshTokenParam.setIpAddress(param.getIpAddress());
        authRefreshTokenRepository.saveRecord(upsertAuthRefreshTokenParam);
        return refreshTokenRaw;
    }

    @Override
    public String createRefreshToken(String refreshTokenRaw, CreateRefreshTokenParam param) {
        if (StringUtils.isBlank(refreshTokenRaw)) {
            throw new BasicRuntimeException("刷新令牌不能为空");
        }
        param.validate();
        RefreshToken refreshToken = RefreshToken.of(refreshTokenRaw);

        UpsertAuthRefreshTokenParam upsertAuthRefreshTokenParam = new UpsertAuthRefreshTokenParam();
        upsertAuthRefreshTokenParam.setId(IdUtils.simpleUuid());
        upsertAuthRefreshTokenParam.setTokenHash(refreshToken.getHash());
        upsertAuthRefreshTokenParam.setSessionId(param.getSessionId());
        upsertAuthRefreshTokenParam.setAccountId(param.getAccountId());
        upsertAuthRefreshTokenParam.setUserId(param.getUserId());
        upsertAuthRefreshTokenParam.setExpiresTime(param.getExpirationTime());
        upsertAuthRefreshTokenParam.setUsed(false);
        upsertAuthRefreshTokenParam.setCreatedTime(param.getIssuedAt());
        upsertAuthRefreshTokenParam.setRevoked(false);
        upsertAuthRefreshTokenParam.setDeviceId(param.getDeviceId());
        upsertAuthRefreshTokenParam.setUserAgent(param.getUserAgent());
        upsertAuthRefreshTokenParam.setIpAddress(param.getIpAddress());
        authRefreshTokenRepository.saveRecord(upsertAuthRefreshTokenParam);
        return refreshTokenRaw;
    }

    @Override
    public AccessTokenJwtPayload parseAccessToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(accessTokenSecretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
            AccessTokenJwtPayload payload = new AccessTokenJwtPayload();
            payload.setSessionId(claims.get(CLAIMS_KEY_SESSION_ID, String.class));
            payload.setAccountId(claims.get(CLAIMS_KEY_ACCOUNT_ID, String.class));
            payload.setUserId(claims.get(CLAIMS_KEY_USER_ID, String.class));
            return payload;
        } catch (ExpiredJwtException e) {
            throw BasicRuntimeException.unauthorized();
        } catch (Exception e) {
            log.error("解析访问令牌时出错", e);
            throw BasicRuntimeException.unauthorized();
        }
    }

    @Override
    public AccessTokenJwtPayload extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            return parseAccessToken(token);
        }
        return null;
    }

    @Override
    public RotateAccessTokenResult rotateAccessToken(String refreshTokenRaw) {
        // 基本校验
        if (StringUtils.isBlank(refreshTokenRaw)) {
            return RotateAccessTokenResult.failure(RotateAccessTokenResult.ResultCode.TOKEN_NOT_FOUND);
        }
        RefreshToken refreshToken = RefreshToken.of(refreshTokenRaw);
        String tokenHash = refreshToken.getHash();
        OffsetDateTime currentTime = OffsetDateTime.now();
        AuthRefreshTokenModel authRefreshTokenModel = authRefreshTokenRepository.getRecordByTokenHash(tokenHash);
        if (authRefreshTokenModel == null) { // 刷新令牌不存在
            return RotateAccessTokenResult.failure(RotateAccessTokenResult.ResultCode.TOKEN_NOT_FOUND);
        }
        if (authRefreshTokenModel.getRevoked()) { // 刷新令牌已作废
            return RotateAccessTokenResult.failure(RotateAccessTokenResult.ResultCode.TOKEN_REVOKED);
        }
        if (authRefreshTokenModel.getExpiresTime().isBefore(currentTime)) { // 刷新令牌已过期
            return RotateAccessTokenResult.failure(RotateAccessTokenResult.ResultCode.TOKEN_EXPIRED);
        }

        // 判断是否已被使用
        if (authRefreshTokenModel.getUsed()) { // 刷新令牌已使用
            // 判断是否在宽限期内
            Duration refreshTokenRotateGraceWindow = expirationConfig.getRefreshTokenRotateGraceWindow();
            OffsetDateTime deadlineTime = authRefreshTokenModel.getUsedTime().plus(refreshTokenRotateGraceWindow);
            if (currentTime.isAfter(deadlineTime)) {
                authRefreshTokenRepository.updateRevokedForUnusedTokenByUserId(
                        authRefreshTokenModel.getUserId(),
                        true,
                        currentTime
                );
                return RotateAccessTokenResult.failure(RotateAccessTokenResult.ResultCode.REPLAY_ATTACK);
            }
            return RotateAccessTokenResult.failure(RotateAccessTokenResult.ResultCode.CONFLICT_IN_GRACE_WINDOW);
        }

        // 生成新的一套访问令牌和刷新令牌
        RotateUserTokenHolder userTokenResultHolder = transactionHelper.executeWithResult(() -> {
            String newRefreshTokenRaw = IdUtils.uuid();
            RefreshToken newRefreshToken = RefreshToken.of(newRefreshTokenRaw);

            // 标记旧的令牌
            int affectedRow = authRefreshTokenRepository.updateUsedByTokenHashIfUnused(
                    tokenHash,
                    true,
                    currentTime,
                    newRefreshToken.getHash()
            );

            RotateUserTokenHolder holder = new RotateUserTokenHolder();
            holder.setAffectedRows(affectedRow);
            if (affectedRow == 0) {
                return holder;
            }

            // 创建新的访问令牌
            CreateAccessTokenParam createAccessTokenParam = new CreateAccessTokenParam();
            createAccessTokenParam.setSessionId(authRefreshTokenModel.getSessionId());
            createAccessTokenParam.setUserId(authRefreshTokenModel.getUserId());
            createAccessTokenParam.setAccountId(authRefreshTokenModel.getAccountId());
            createAccessTokenParam.setIssuedAt(currentTime);
            createAccessTokenParam.setExpirationTime(currentTime.plus(expirationConfig.getAccessToken()));
            String newAccessTokenRaw = createAccessToken(createAccessTokenParam);

            // 创建新的刷新令牌
            CreateRefreshTokenParam createRefreshTokenParam = new CreateRefreshTokenParam();
            createRefreshTokenParam.setSessionId(authRefreshTokenModel.getSessionId());
            createRefreshTokenParam.setUserId(authRefreshTokenModel.getUserId());
            createRefreshTokenParam.setAccountId(authRefreshTokenModel.getAccountId());
            createRefreshTokenParam.setDeviceId(authRefreshTokenModel.getDeviceId());
            createRefreshTokenParam.setUserAgent(authRefreshTokenModel.getUserAgent());
            createRefreshTokenParam.setIpAddress(authRefreshTokenModel.getIpAddress());
            createRefreshTokenParam.setIssuedAt(currentTime);
            createRefreshTokenParam.setExpirationTime(currentTime.plus(expirationConfig.getRefreshToken()));
            newRefreshTokenRaw = createRefreshToken(newRefreshTokenRaw, createRefreshTokenParam);

            // 组装结果
            UserTokenResult userTokenResult = new UserTokenResult();
            userTokenResult.setAccessToken(newAccessTokenRaw);
            userTokenResult.setRefreshToken(newRefreshTokenRaw);
            holder.setUserTokenResult(userTokenResult);
            return holder;
        });

        if (userTokenResultHolder.getAffectedRows() == 0) { // 说明有其他请求先标记已使用了，回头查一下记录
            AuthRefreshTokenModel newAuthRefreshTokenModel = authRefreshTokenRepository.getRecordByTokenHash(tokenHash);
            if (newAuthRefreshTokenModel == null) {
                return RotateAccessTokenResult.failure(RotateAccessTokenResult.ResultCode.TOKEN_NOT_FOUND);
            }
            if (newAuthRefreshTokenModel.getUsed()) {
                // 判断是否在宽限期内
                Duration refreshTokenRotateGraceWindow = expirationConfig.getRefreshTokenRotateGraceWindow();
                OffsetDateTime deadlineTime = newAuthRefreshTokenModel.getUsedTime().plus(refreshTokenRotateGraceWindow);
                if (currentTime.isAfter(deadlineTime)) {
                    authRefreshTokenRepository.updateRevokedForUnusedTokenByUserId(
                            authRefreshTokenModel.getUserId(),
                            true,
                            currentTime
                    );
                    return RotateAccessTokenResult.failure(RotateAccessTokenResult.ResultCode.REPLAY_ATTACK);
                }
                return RotateAccessTokenResult.failure(RotateAccessTokenResult.ResultCode.CONFLICT_IN_GRACE_WINDOW);
            }
            log.warn("轮换令牌: affectedRow=0但是重新查询时used=false，状态不一致，tokenHash={}", tokenHash);
            authRefreshTokenRepository.updateRevokedForUnusedTokenByUserId(
                    authRefreshTokenModel.getUserId(),
                    true,
                    currentTime
            );
            return RotateAccessTokenResult.failure(RotateAccessTokenResult.ResultCode.REPLAY_ATTACK); // 兜底
        }
        UserTokenResult userTokenResult = userTokenResultHolder.getUserTokenResult();

        if (userTokenResult == null) {
            throw new BasicRuntimeException("生成用户令牌失败");
        }

        return RotateAccessTokenResult.success(userTokenResult.getRefreshToken(), userTokenResult.getAccessToken());
    }

    /**
     * 轮换用户令牌的持有者
     */
    @Data
    static class RotateUserTokenHolder implements Serializable {
        /**
         * 影响行数
         */
        private int affectedRows;

        /**
         * 用户令牌结果
         */
        private UserTokenResult userTokenResult;
    }
}
