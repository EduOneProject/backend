package io.github.eduoneproject.eduone.query.kernel.service.region.assembler;

import io.github.eduoneproject.eduone.query.kernel.repository.region.dto.RegionTreeDto;
import io.github.eduoneproject.eduone.query.kernel.service.region.model.RegionTreeModel;

/**
 * 地区树模型组装器
 *
 * @author summerain0
 */
public class RegionTreeAssembler {
    public static RegionTreeModel assemble(RegionTreeDto source) {
        if (source == null) {
            return null;
        }
        RegionTreeModel target = new RegionTreeModel();
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
