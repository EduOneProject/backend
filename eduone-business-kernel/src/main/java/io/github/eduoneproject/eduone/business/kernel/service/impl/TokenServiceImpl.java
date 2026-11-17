package io.github.eduoneproject.eduone.business.kernel.service.impl;

import io.github.eduoneproject.eduone.business.common.config.ExpirationConfig;
import io.github.eduoneproject.eduone.business.kernel.service.TokenService;
import io.github.eduoneproject.eduone.business.kernel.service.model.AccessTokenJwtPayload;
import io.github.eduoneproject.eduone.business.kernel.service.model.UserTokenResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateAccessTokenParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateRefreshTokenParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.CreateUserTokenParam;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
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

    @Override
    public UserTokenResult generateUserToken(CreateUserTokenParam param) {
        param.validate();

        // 生成访问令牌
        CreateAccessTokenParam createAccessTokenParam = new CreateAccessTokenParam();
        createAccessTokenParam.setUserId(param.getUserId());
        createAccessTokenParam.setAccountId(param.getAccountId());
        createAccessTokenParam.setSessionId(param.getSessionId());
        createAccessTokenParam.setIssuedAt(param.getIssuedAt());
        createAccessTokenParam.setExpirationTime(param.getExpirationTime());
        String accessToken = createAccessToken(createAccessTokenParam);

        // 生成刷新令牌
        CreateRefreshTokenParam createRefreshTokenParam = new CreateRefreshTokenParam();
        createRefreshTokenParam.setUserId(param.getUserId());
        String refreshToken = createRefreshToken(createRefreshTokenParam);

        UserTokenResult result = new UserTokenResult();
        result.setUserId(param.getUserId());
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);
        return result;
    }

    @Override
    public String createAccessToken(CreateAccessTokenParam param) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
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
        if (StringUtils.isBlank(param.getUserId())) {
            throw new BasicRuntimeException("用户ID不能为空");
        }

        String id = UUID.randomUUID().toString();
        String hash = DigestUtils.sha256Hex(id);
        OffsetDateTime currentDate = OffsetDateTime.now();
        OffsetDateTime expirationDate = currentDate.plus(expirationConfig.getRefreshToken());
        return id;
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
}
