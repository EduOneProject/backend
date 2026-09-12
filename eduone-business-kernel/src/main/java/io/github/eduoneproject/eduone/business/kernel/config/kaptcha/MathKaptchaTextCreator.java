package io.github.eduoneproject.eduone.business.kernel.config.kaptcha;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.util.Random;

/**
 * 四则运算验证码
 *
 * @author summerain0
 */
@SuppressWarnings("unused")
public class MathKaptchaTextCreator extends DefaultTextCreator {
    @Override
    public String getText() {
        Random random = new Random();
        // 生成两个10以内的数
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        // 运算符
        int operands = (int) Math.floor(Math.random() * 4); // 0+ 1- 2* 3/
        // 计算结果
        int result;
        // 验证码内容
        StringBuilder captchaTextBuilder = new StringBuilder();
        switch (operands) {
            case 1 -> { // -
                if (x >= y) {
                    result = x - y;
                    captchaTextBuilder.append(x).append("-").append(y);
                } else {
                    result = y - x;
                    captchaTextBuilder.append(y).append("-").append(x);
                }
            }
            case 2 -> { // *
                result = x * y;
                captchaTextBuilder.append(x).append("*").append(y);
            }
            case 3 -> { // /
                if (y != 0 && x % y == 0) {
                    result = x / y;
                    captchaTextBuilder.append(x).append("/").append(y);
                } else { // 不能整除的用加法代替
                    result = x + y;
                    captchaTextBuilder.append(x).append("+").append(y);
                }
            }
            default -> { // 默认为加法
                result = x + y;
                captchaTextBuilder.append(x).append("+").append(y);
            }
        }
        captchaTextBuilder.append("=?@").append(result);
        return captchaTextBuilder.toString();
    }
}