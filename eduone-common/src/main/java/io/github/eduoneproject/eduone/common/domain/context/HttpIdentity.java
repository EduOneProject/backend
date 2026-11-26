package io.github.eduoneproject.eduone.common.domain.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * http标识信息
 *
 * @author summerain0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpIdentity implements Serializable {
    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 请求域名
     */
    private String domain;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 用户代理标识
     */
    private String userAgent;
}
