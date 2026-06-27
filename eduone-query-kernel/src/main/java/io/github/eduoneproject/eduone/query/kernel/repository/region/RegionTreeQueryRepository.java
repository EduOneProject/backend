package io.github.eduoneproject.eduone.query.kernel.repository.region;

import io.github.eduoneproject.eduone.query.kernel.repository.region.dto.RegionTreeDto;
import io.github.eduoneproject.eduone.query.kernel.repository.region.param.RegionTreeRParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 地区树仓储层
 *
 * @author summerain0
 */
@Mapper
public interface RegionTreeQueryRepository {
    /**
     * 查询地区列表
     *
     * @param param 地区查询参数
     * @return 地区信息列表
     */
    List<RegionTreeDto> selectList(RegionTreeRParam param);
}