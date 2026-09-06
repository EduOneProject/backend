package io.github.eduoneproject.eduone.common.util;

import io.github.eduoneproject.eduone.business.common.domain.verification.VerificationContext;
import io.github.eduoneproject.eduone.business.common.domain.verification.VerificationStep;
import io.github.eduoneproject.eduone.business.common.enums.verification.VerificationScene;
import io.github.eduoneproject.eduone.business.common.enums.verification.VerificationType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证上下文测试类
 *
 * @author summerain0
 */
@SpringBootTest
public class VerificationContextTest {
    /**
     * 生成管理员登录验证上下文
     */
    @Test
    public void generateAdminLoginToken() {
        VerificationContext verificationContext = new VerificationContext();
        List<VerificationStep> steps = new ArrayList<>();
        VerificationStep imageCaptchaStep = new VerificationStep();
        imageCaptchaStep.setVerificationScene(VerificationScene.ADMIN_LOGIN);
        imageCaptchaStep.setVerificationType(VerificationType.IMAGE_CAPTCHA);
        steps.add(imageCaptchaStep);
        verificationContext.setSteps(steps);
        System.out.println(VerificationContext.encrypt(verificationContext));
    }

    /**
     * 解密验证上下文
     */
    @Test
    public void decryptToken() {
        String token = "F863E06BCE179BA1CAB220B29003438DF34DB54F127749A135C069798BFC431DCCAAF6CF27530BD2C1E5A0430B17C9A128FC95619521FC8A56B374E670371436DEA13F0879591798484801B4C81940F252E9A24EF8C9854FC1FDA4EB30E6E38FF184BC1E59E2DFCA8E4BCBCA48682BD73C6DA281D18DD9AAB17634671D13CBA7097711B047D3E8239909FCA542B65FAAA41CDB52DA5ED38E4CC59B1DAFC9F4817D19489AD0997AA967295A4327D45DDED1C1E5EB12139A243052412DFE639853D60D868BC86DEB6D791A5E5294141CB4B78F4B9336B3A3DEFCA411B8255488F73EA8D2CA50D9E67F020B7A86E873EAEB6FEEB2D50A40A512914E7AABC5F8A6D6CCA17A9147267E6DF850CC0039145DD5ED8F01763484729266AEA01C1EAE102B4FE0E829594AB96CAE7FEBF71F35DBD6DA5042E956C631269101861910DE9B96E40849689699A93014A8ADF5849128A18D28507AF7A5033951FA17C0696E0B1848BF4572626CC2EFE4D12F0469AA823D636E1150370DE9E3AA6C1C45E193C1A706BF6548A722D835990009877E63AF2DBEFAB2C44032";
        VerificationContext verificationContext = VerificationContext.decrypt(token);
        assert verificationContext != null;
    }
}
