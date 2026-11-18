package io.github.eduoneproject.eduone.business.kernel.service.model;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;

/**
 * 刷新令牌
 *
 * @author summerain0
 */
@Data
public class RefreshToken implements Serializable {
    /**
     * 原始令牌
     */
    private String raw;

    /**
     * 令牌哈希值
     */
    private String hash;

    private RefreshToken() {
    }

    /**
     * 生成刷新令牌
     *
     * @param raw 刷新令牌的原始字符串
     * @return 刷新令牌
     */
    public static RefreshToken of(String raw) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.raw = raw;
        refreshToken.hash = DigestUtils.sha256Hex(raw);
        return refreshToken;
    }
}