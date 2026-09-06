package io.github.eduoneproject.eduone.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES-GCM 加解密工具类
 *
 * @author summerain0
 */
public class AESGCMUtils {
    private static final String ALGORITHM = "AES/GCM/NOPADDING";
    private static final String KEY_ALGORITHM = "AES";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;
    private static final int AES_KEY_SIZE = 256;

    /**
     * 生成AES密钥
     *
     * @return AES密钥信息
     */
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGen.init(AES_KEY_SIZE);
        return keyGen.generateKey();
    }

    /**
     * 将密钥转换为Base64字符串
     *
     * @return Base64编码的密钥字符串
     */
    public static String keyToBase64(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 从Base64字符串恢复密钥
     *
     * @return 恢复的AES密钥
     */
    public static SecretKey keyFromBase64(String base64Key) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param plaintext 明文字节数组
     * @param key       AES密钥
     * @return 加密后的字节数组 (IV + 密文)
     */
    public static byte[] encrypt(byte[] plaintext, SecretKey key) throws Exception {
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] ciphertext = cipher.doFinal(plaintext);
        byte[] result = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);
        return result;
    }

    /**
     * 解密
     *
     * @param ciphertextWithIv 密文字节数组 (IV + 密文)
     * @param key              AES密钥
     * @return 明文字节数组
     */
    public static byte[] decrypt(byte[] ciphertextWithIv, SecretKey key) throws Exception {
        if (ciphertextWithIv.length < IV_LENGTH) {
            throw new IllegalArgumentException("密文长度不正确，至少需要" + IV_LENGTH + "字节");
        }
        byte[] iv = new byte[IV_LENGTH];
        byte[] ciphertext = new byte[ciphertextWithIv.length - IV_LENGTH];
        System.arraycopy(ciphertextWithIv, 0, iv, 0, IV_LENGTH);
        System.arraycopy(ciphertextWithIv, IV_LENGTH, ciphertext, 0, ciphertext.length);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return cipher.doFinal(ciphertext);
    }
}