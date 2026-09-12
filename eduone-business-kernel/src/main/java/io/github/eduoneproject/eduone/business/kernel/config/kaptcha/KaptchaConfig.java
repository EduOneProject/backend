package io.github.eduoneproject.eduone.business.kernel.config.kaptcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * 验证码配置类
 *
 * @author summerain0
 */
@Configuration
public class KaptchaConfig {
    /**
     * 数学运算图形验证码配置
     */
    @Bean(name = "captchaProductMath")
    public DefaultKaptcha getKaptchaBeanMath() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // 边框颜色
        properties.setProperty(KAPTCHA_BORDER_COLOR, "128,128,128");
        // 验证码文本字符颜色
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        // 验证码图片宽度
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        // 验证码图片高度
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        // 验证码文本字符大小
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        // kaptcha会话标识
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        // 验证码文本生成器
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, MathKaptchaTextCreator.class.getName());
        // 验证码文本字符间距
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        // 验证码文本字符长度
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        // 验证码文本字体样式
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // 验证码噪点颜色
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        // 干扰实现类
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        // 图片样式
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}