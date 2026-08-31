package io.github.eduoneproject.eduone.query.gateway.assembler;

import io.github.eduoneproject.eduone.query.gateway.response.RegionTreeResponse;
import io.github.eduoneproject.eduone.query.kernel.service.region.model.RegionTreeModel;

/**
 * 地区树模型组装器
 *
 * @author summerain0
 */
public class RegionTreeGatewayAssembler {
    public static RegionTreeResponse assemble(RegionTreeModel source) {
        if (source == null) {
            return null;
        }
        RegionTreeResponse target = new RegionTreeResponse();
        target.setId(source.getId());
        target.setCode(source.getCode());
        target.setParentId(source.getParentId());
        target.setParentCode(source.getParentCode());
        target.setLevel(source.getLevel());
        target.setPath(source.getPath());
        target.setName(source.getName());
        target.setSort(source.getSort());
        target.setStatus(source.getStatus());
        return target;
    }
}
