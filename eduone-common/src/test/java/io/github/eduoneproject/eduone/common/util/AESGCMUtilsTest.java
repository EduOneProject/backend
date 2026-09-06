package io.github.eduoneproject.eduone.common.util;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

/**
 * AES-GCM 加解密工具类测试
 *
 * @author summerain0
 */
public class AESGCMUtilsTest {
    /**
     * 测试生成密钥
     */
    @Test
    public void generateKey() throws Exception {
        SecretKey secretKey = AESGCMUtils.generateKey();
        System.out.println(AESGCMUtils.keyToBase64(secretKey));
    }
}
