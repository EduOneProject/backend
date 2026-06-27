package io.github.eduoneproject.eduone.query.gateway.converter;

import io.github.eduoneproject.eduone.query.gateway.request.RegionTreeQueryRequest;
import io.github.eduoneproject.eduone.query.kernel.repository.region.param.RegionTreeRParam;
import io.github.eduoneproject.eduone.query.kernel.service.region.param.RegionTreeKParam;

/**
 * 地区树模型转换器
 *
 * @author summerain0
 */
public class RegionTreeGatewayConverter {
    public static RegionTreeKParam convert(RegionTreeQueryRequest source) {
        if (source == null) {
            return new RegionTreeKParam();
        }
        RegionTreeKParam target = new RegionTreeKParam();
        target.setIdList(source.getIdList());
        target.setCodeList(source.getCodeList());
        target.setParentIdList(source.getParentIdList());
        target.setParentCodeList(source.getParentCodeList());
        target.setStatus(source.getStatus());
        return target;
    }
}
