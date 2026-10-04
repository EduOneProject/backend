package io.github.eduoneproject.eduone.business.kernel.service.impl;

import io.github.eduoneproject.eduone.business.common.config.ExpirationConfig;
import io.github.eduoneproject.eduone.business.kernel.repository.AuthRefreshTokenRepository;
import io.github.eduoneproject.eduone.business.kernel.repository.pojo.AuthRefreshTokenPO;
import io.github.eduoneproject.eduone.business.kernel.service.TokenService;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateRefreshTokenParam;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
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
    @Qualifier("accessTokenSecretKey")
    private final SecretKey accessTokenSecretKey;
    private final ExpirationConfig expirationConfig;
    private final AuthRefreshTokenRepository authRefreshTokenRepository;

    @Override
    public String createAccessToken(String userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .id(UUID.randomUUID().toString())
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(expirationConfig.getAccessToken())))
                .signWith(accessTokenSecretKey)
                .compact();
    }

    @Override
    public String createRefreshToken(CreateRefreshTokenParam param) {
        if (StringUtils.isBlank(param.getUserId())) {
            throw new BasicRuntimeException("用户ID不能为空");
        }

        String id = UUID.randomUUID().toString();
        String hash = DigestUtils.sha256Hex(id);
        OffsetDateTime currentDate = OffsetDateTime.now();
        OffsetDateTime expirationDate = currentDate.plus(expirationConfig.getRefreshToken());

        AuthRefreshTokenPO pojo = new AuthRefreshTokenPO();
        pojo.setId(id);
        pojo.setTokenHash(hash);
        pojo.setUserId(param.getUserId());
        pojo.setExpiresTime(expirationDate);
        pojo.setUsed(false);
        pojo.setCreatedTime(currentDate);
        pojo.setRevoked(false);
        pojo.setDeviceId(param.getDeviceId());
        pojo.setUserAgent(param.getUserAgent());
        pojo.setIpAddress(param.getIpAddress());
        authRefreshTokenRepository.insert(pojo);

        return id;
    }

    @Override
    public Claims parseAccessToken(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(accessTokenSecretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw BasicRuntimeException.unauthorized();
        } catch (Exception e) {
            log.error("解析访问令牌时出错", e);
            throw BasicRuntimeException.unauthorized();
        }
    }
}
