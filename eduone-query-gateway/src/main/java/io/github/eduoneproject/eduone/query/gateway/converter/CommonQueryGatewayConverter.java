package io.github.eduoneproject.eduone.query.gateway.converter;

import io.github.eduoneproject.eduone.common.domain.DataPage;
import io.github.eduoneproject.eduone.query.gateway.request.PageRequest;

/**
 * 通用查询网关模型转换器
 *
 * @author summerain0
 */
public class CommonQueryGatewayConverter {
    /**
     * 分页参数转换
     *
     * @param request 分页参数
     * @return 分页参数
     */
    public static DataPage<?> convert(PageRequest request) {
        return new DataPage<>(request.getPageNo(), request.getPageSize());
    }
}
