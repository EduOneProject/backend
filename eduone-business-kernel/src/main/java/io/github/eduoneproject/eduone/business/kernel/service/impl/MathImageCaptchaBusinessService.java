package io.github.eduoneproject.eduone.business.kernel.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.github.eduoneproject.eduone.business.common.config.ExpirationConfig;
import io.github.eduoneproject.eduone.business.common.consts.BusinessExceptionCode;
import io.github.eduoneproject.eduone.business.common.support.RedisCache;
import io.github.eduoneproject.eduone.business.kernel.service.CaptchaBusinessService;
import io.github.eduoneproject.eduone.business.kernel.service.model.CaptchaGenerationResult;
import io.github.eduoneproject.eduone.business.kernel.service.model.CaptchaValidationResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.CaptchaValidationParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.MathImageCaptchaGenerationParam;
import io.github.eduoneproject.eduone.common.consts.CommonExceptionCode;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

/**
 * 四则运算图形验证码业务接口实现
 *
 * @author summerain0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MathImageCaptchaBusinessService implements CaptchaBusinessService<String, MathImageCaptchaGenerationParam> {
    /**
     * 数学验证码生产器
     */
    @Qualifier("captchaProductMath")
    private final DefaultKaptcha captchaProductMath;

    /**
     * Redis缓存
     */
    private final RedisCache redisCache;

    /**
     * 验证码过期配置
     */
    private final ExpirationConfig expirationConfig;

    @Override
    public CaptchaGenerationResult<String> generate(MathImageCaptchaGenerationParam param) {
        try {
            String captchaOriginContent = captchaProductMath.createText();
            // 截取出正确的验证码
            int index = captchaOriginContent.indexOf("@");
            // 防止验证码串出问题影响后续代码
            if (index == -1) {
                log.error("验证码生成的四则运算串非法。生成结果：{}", captchaOriginContent);
                throw new BasicRuntimeException("验证码生成异常");
            }
            String question = captchaOriginContent.substring(0, index);
            String answer = captchaOriginContent.substring(index + 1);
            // 创建验证码图片
            FastByteArrayOutputStream out = new FastByteArrayOutputStream();
            BufferedImage bufferedImage = captchaProductMath.createImage(question);
            ImageIO.write(bufferedImage, "jpg", out);
            String imageBase64 = Base64.getEncoder().encodeToString(out.toByteArray());
            // 缓存结果
            String uniqueId = UUID.randomUUID().toString();
            OffsetDateTime offsetDateTime = OffsetDateTime.now();
            Duration duration = expirationConfig.getImageCaptcha();
            redisCache.setCacheObject(getRedisKey(uniqueId), answer, duration);
            // 封装结果
            CaptchaGenerationResult<String> result = new CaptchaGenerationResult<>();
            result.setUniqueId(uniqueId);
            result.setData(imageBase64);
            result.setTtl(duration.getSeconds());
            result.setExpirationTime(offsetDateTime.plusSeconds(duration.getSeconds()));
            return result;
        } catch (Exception e) {
            throw new BasicRuntimeException("验证码生成异常", e);
        }
    }

    @Override
    public CaptchaValidationResult validate(CaptchaValidationParam param) {
        String uniqueId = param.getUniqueId();
        String userInput = param.getUserInput();

        // 基础校验
        if (StringUtils.isBlank(uniqueId)) {
            throw new BasicRuntimeException(CommonExceptionCode.PARAM_REQUIRED.getCode(), "验证码唯一标识为空");
        }
        String redisKey = getRedisKey(uniqueId);
        boolean existsKey = redisCache.existsKey(redisKey);
        if (!existsKey) {
            throw new BasicRuntimeException(
                    BusinessExceptionCode.CAPTCHA_EXPIRED.getCode(),
                    BusinessExceptionCode.CAPTCHA_EXPIRED.getDescription()
            );
        }
        String correctAnswer = redisCache.getCacheObject(redisKey);

        redisCache.deleteCacheObject(redisKey);
        boolean valid = Objects.equals(userInput, correctAnswer);

        CaptchaValidationResult result = new CaptchaValidationResult();
        result.setUniqueId(uniqueId);
        result.setValid(valid);
        return result;
    }

    /**
     * 获取Redis缓存的key
     *
     * @param uniqueId 唯一标识
     * @return Redis缓存的key
     */
    private String getRedisKey(String uniqueId) {
        return "imageCaptcha:" + uniqueId;
    }
}
