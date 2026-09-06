package io.github.eduoneproject.eduone.business.common.domain.verification;

import io.github.eduoneproject.eduone.business.common.config.CryptoKeyConfig;
import io.github.eduoneproject.eduone.business.common.enums.verification.VerificationType;
import io.github.eduoneproject.eduone.common.util.AESGCMUtils;
import io.github.eduoneproject.eduone.common.util.SpringUtils;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.crypto.SecretKey;
import java.io.*;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 验证上下文
 *
 * @author summerain0
 */
@Data
public class VerificationContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 验证步骤列表
     */
    private List<VerificationStep> steps;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 整体过期时间
     */
    private Long overallExpireTime;

    /**
     * 根据场景和类型获取验证步骤
     *
     * @param scene 验证场景
     * @param type  验证类型
     * @return 验证步骤
     */
    public VerificationStep getStep(String scene, VerificationType type) {
        if (CollectionUtils.isEmpty(steps)) {
            return null;
        }
        return steps.stream()
                .filter(step -> Objects.equals(step.getVerificationScene(), scene) && Objects.equals(step.getVerificationType(), type))
                .findFirst()
                .orElse(null);
    }

    /**
     * 检查验证是否完整
     *
     * @param verificationContext 验证上下文
     */
    public static void checkFullyVerified(VerificationContext verificationContext) {
        long currentTime = System.currentTimeMillis();

        for (VerificationStep step : verificationContext.getSteps()) {
            // 过期检查
            if (currentTime > step.getExpireTime()) {
                throw new RuntimeException(step.getVerificationType().getDescription() + "已过期，请重新验证");
            }
            // 完成检查
            if (!step.isCompleted()) {
                throw new RuntimeException(step.getVerificationType().getDescription() + "未完成，请重新验证");
            }
        }
    }

    /**
     * 解密Token
     *
     * @param token Token字符串
     * @return 解密后的Token信息
     */
    public static VerificationContext decrypt(String token) {
        try {
            SecretKey secretKey = getSymmetricCrypto();
            byte[] bytes = HexFormat.of().parseHex(token);
            byte[] decryptBytes = AESGCMUtils.decrypt(bytes, secretKey);
            try (
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptBytes);
                    GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
            ) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = gzipInputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                byte[] decompressedBytes = byteArrayOutputStream.toByteArray();
                // 反序列化对象
                try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(decompressedBytes))) {
                    Object obj = objectInputStream.readObject();
                    return (VerificationContext) obj;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("解密Token失败", e);
        }
    }

    /**
     * 加密Token
     *
     * @param token Token信息
     * @return 加密后的Token字符串
     */
    public static String encrypt(VerificationContext token) {
        if (token == null) return null;
        try (
                ByteArrayOutputStream serialByteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(serialByteArrayOutputStream)
        ) {
            objectOutputStream.writeObject(token);
            objectOutputStream.flush(); // 确保数据已写入
            byte[] bytes = serialByteArrayOutputStream.toByteArray();
            // 这里不直接用构造器传是因为GZIP和Object可能冲突
            try (
                    ByteArrayOutputStream compressedByteArrayOutputStream = new ByteArrayOutputStream();
                    GZIPOutputStream gzipOutputStream = new GZIPOutputStream(compressedByteArrayOutputStream)
            ) {
                gzipOutputStream.write(bytes);
                gzipOutputStream.close(); // 结束压缩
                byte[] compressedBytes = compressedByteArrayOutputStream.toByteArray();
                SecretKey secretKey = getSymmetricCrypto();
                byte[] encrypt = AESGCMUtils.encrypt(compressedBytes, secretKey);
                return HexFormat.of().formatHex(encrypt).toUpperCase();
            }
        } catch (Exception e) {
            throw new RuntimeException("加密Token失败", e);
        }
    }

    /**
     * 获取加解密密钥
     *
     * @return 加解密密钥
     */
    private static SecretKey getSymmetricCrypto() {
        CryptoKeyConfig cryptoKeyConfig = SpringUtils.getBean(CryptoKeyConfig.class);
        return AESGCMUtils.keyFromBase64(cryptoKeyConfig.getVerificationContext());
    }
}
