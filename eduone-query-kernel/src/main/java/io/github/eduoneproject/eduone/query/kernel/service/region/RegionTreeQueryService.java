package io.github.eduoneproject.eduone.query.kernel.service.region;

import io.github.eduoneproject.eduone.query.kernel.service.region.dto.RegionTreeModel;
import io.github.eduoneproject.eduone.query.kernel.service.region.param.RegionTreeKParam;

import java.util.List;

/**
 * 地区树查询服务接口
 *
 * @author summerain0
 */
public interface RegionTreeQueryService {
    /**
     * 查询地区列表
     *
     * @param param 地区查询参数
     * @return 地区信息列表
     */
    List<RegionTreeModel> getRegionTreeList(RegionTreeKParam param);
}
