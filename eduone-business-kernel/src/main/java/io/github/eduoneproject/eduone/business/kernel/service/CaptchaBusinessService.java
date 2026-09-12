package io.github.eduoneproject.eduone.business.kernel.service;

import io.github.eduoneproject.eduone.business.kernel.service.model.CaptchaGenerationResult;
import io.github.eduoneproject.eduone.business.kernel.service.model.CaptchaValidationResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.CaptchaGenerationParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.CaptchaValidationParam;

/**
 * 验证码服务接口
 *
 * @author summerain0
 */
public interface CaptchaBusinessService<T, P extends CaptchaGenerationParam> {
    /**
     * 生成验证码
     *
     * @param param 验证码生成参数
     * @return 验证码生成结果
     */
    CaptchaGenerationResult<T> generate(P param);

    /**
     * 校验验证码
     *
     * @param param 验证码校验参数
     * @return 验证码校验结果
     */
    CaptchaValidationResult validate(CaptchaValidationParam param);
}
