package io.github.eduoneproject.eduone.business.kernel.appservice;

import io.github.eduoneproject.eduone.business.kernel.service.model.LoginSuccessResult;
import io.github.eduoneproject.eduone.business.kernel.service.param.LoginWithPasswordParam;

/**
 * 认证业务类
 *
 * @author summerain0
 */
public interface AuthAppService {
    /**
     * 登录
     *
     * @param param 登录参数
     * @return 登录结果
     */
    LoginSuccessResult loginWithPassword(LoginWithPasswordParam param);
}
