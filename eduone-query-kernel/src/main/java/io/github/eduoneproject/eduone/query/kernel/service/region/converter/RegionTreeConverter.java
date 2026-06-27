package io.github.eduoneproject.eduone.query.kernel.service.region.converter;

import io.github.eduoneproject.eduone.query.kernel.repository.region.param.RegionTreeRParam;
import io.github.eduoneproject.eduone.query.kernel.service.region.param.RegionTreeKParam;

/**
 * 地区树模型转换器
 *
 * @author summerain0
 */
public class RegionTreeConverter {
    public static RegionTreeRParam convert(RegionTreeKParam source) {
        if (source == null) {
            return new RegionTreeRParam();
        }
        RegionTreeRParam target = new RegionTreeRParam();
        target.setIdList(source.getIdList());
        target.setCodeList(source.getCodeList());
        target.setParentIdList(source.getParentIdList());
        target.setParentCodeList(source.getParentCodeList());
        target.setStatus(source.getStatus());
        return target;
    }
}
