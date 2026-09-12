package io.github.eduoneproject.eduone.business.gateway.resolver;

import io.github.eduoneproject.eduone.business.common.consts.BusinessExceptionCode;
import io.github.eduoneproject.eduone.business.common.domain.verification.VerificationContext;
import io.github.eduoneproject.eduone.business.common.domain.verification.VerificationStep;
import io.github.eduoneproject.eduone.business.common.enums.verification.VerificationType;
import io.github.eduoneproject.eduone.business.gateway.request.ApplyImageCaptchaRequest;
import io.github.eduoneproject.eduone.business.gateway.request.ImageCaptchaValidRequest;
import io.github.eduoneproject.eduone.business.gateway.response.CommonBusinessResponse;
import io.github.eduoneproject.eduone.business.gateway.response.ImageCaptchaResponse;
import io.github.eduoneproject.eduone.business.kernel.service.CaptchaBusinessService;
import io.github.eduoneproject.eduone.business.kernel.service.model.CaptchaGenerationResult;
import io.github.eduoneproject.eduone.business.kernel.service.model.CaptchaValidationResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.CaptchaValidationParam;
import io.github.eduoneproject.eduone.business.kernel.service.param.MathImageCaptchaGenerationParam;
import io.github.eduoneproject.eduone.common.exception.BasicRuntimeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * 验证相关业务接口
 *
 * @author summerain0
 */
@RequiredArgsConstructor
@Controller
public class ValidationMutationResolver {
    @Qualifier("mathImageCaptchaBusinessService")
    private final CaptchaBusinessService<String, MathImageCaptchaGenerationParam> mathImageCaptchaBusinessService;

    /**
     * 请求图形验证码
     *
     * @param request 图形验证码请求参数
     * @return 图形验证码信息
     */
    @MutationMapping
    public CommonBusinessResponse<ImageCaptchaResponse> applyImageCaptcha(@Argument @Valid ApplyImageCaptchaRequest request) {
        // 检查token
        VerificationContext verificationContext = VerificationContext.decrypt(request.getToken());
        if (verificationContext == null) {
            throw new BasicRuntimeException(
                    BusinessExceptionCode.VERIFICATION_CONTEXT_INVALID.getCode(),
                    BusinessExceptionCode.VERIFICATION_CONTEXT_INVALID.getDescription()
            );
        }
        VerificationStep imageCaptchaStep = verificationContext.getStep(request.getVerificationScene(), VerificationType.IMAGE_CAPTCHA);
        if (imageCaptchaStep == null) {
            throw new BasicRuntimeException(
                    BusinessExceptionCode.VERIFICATION_STEP_NOT_REQUIRED.getCode(),
                    BusinessExceptionCode.VERIFICATION_STEP_NOT_REQUIRED.getDescription()
            );
        }
        if (imageCaptchaStep.isCompleted()) {
            throw new BasicRuntimeException(
                    BusinessExceptionCode.VERIFICATION_STEP_ALREADY_VERIFIED.getCode(),
                    BusinessExceptionCode.VERIFICATION_STEP_ALREADY_VERIFIED.getDescription()
            );
        }

        // 生成验证码
        MathImageCaptchaGenerationParam captchaGenerationParam = new MathImageCaptchaGenerationParam();
        CaptchaGenerationResult<String> captchaGenerationResult = mathImageCaptchaBusinessService.generate(captchaGenerationParam);
        imageCaptchaStep.setUniqueId(captchaGenerationResult.getUniqueId());
        imageCaptchaStep.setExpireTime(captchaGenerationResult.getExpirationTime().toInstant().toEpochMilli());

        // 构建响应
        ImageCaptchaResponse response = new ImageCaptchaResponse();
        response.setImageBase64(captchaGenerationResult.getData());
        response.setToken(VerificationContext.encrypt(verificationContext));
        return CommonBusinessResponse.success(response);
    }

    /**
     * 校验图形验证码
     *
     * @param request 图形验证码校验请求参数
     * @return 校验结果
     */
    @MutationMapping
    public CommonBusinessResponse<String> validateImageCaptcha(@Argument @Valid ImageCaptchaValidRequest request) {
        // 检查token
        VerificationContext verificationContext = VerificationContext.decrypt(request.getToken());
        if (verificationContext == null) {
            throw new BasicRuntimeException(
                    BusinessExceptionCode.VERIFICATION_CONTEXT_INVALID.getCode(),
                    BusinessExceptionCode.VERIFICATION_CONTEXT_INVALID.getDescription()
            );
        }
        VerificationStep imageCaptchaStep = verificationContext.getStep(request.getVerificationScene(), VerificationType.IMAGE_CAPTCHA);
        if (imageCaptchaStep == null) {
            throw new BasicRuntimeException(
                    BusinessExceptionCode.VERIFICATION_STEP_NOT_REQUIRED.getCode(),
                    BusinessExceptionCode.VERIFICATION_STEP_NOT_REQUIRED.getDescription()
            );
        }
        if (imageCaptchaStep.isCompleted()) {
            throw new BasicRuntimeException(
                    BusinessExceptionCode.VERIFICATION_STEP_ALREADY_VERIFIED.getCode(),
                    BusinessExceptionCode.VERIFICATION_STEP_ALREADY_VERIFIED.getDescription()
            );
        }

        CaptchaValidationParam captchaValidationParam = new CaptchaValidationParam();
        captchaValidationParam.setUniqueId(imageCaptchaStep.getUniqueId());
        captchaValidationParam.setUserInput(request.getCode());
        CaptchaValidationResult captchaValidationResult = mathImageCaptchaBusinessService.validate(captchaValidationParam);
        if (!captchaValidationResult.isValid()) {
            throw new BasicRuntimeException(
                    BusinessExceptionCode.CAPTCHA_INVALID.getCode(),
                    BusinessExceptionCode.CAPTCHA_INVALID.getDescription()
            );
        }

        imageCaptchaStep.setCompleted(true);
        return CommonBusinessResponse.success(VerificationContext.encrypt(verificationContext));
    }
}
