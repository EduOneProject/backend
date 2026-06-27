package io.github.eduoneproject.eduone.query.kernel.service.region.impl;

import io.github.eduoneproject.eduone.query.common.converter.CommonConverter;
import io.github.eduoneproject.eduone.query.kernel.repository.region.RegionTreeQueryRepository;
import io.github.eduoneproject.eduone.query.kernel.repository.region.dto.RegionTreeDto;
import io.github.eduoneproject.eduone.query.kernel.repository.region.param.RegionTreeRParam;
import io.github.eduoneproject.eduone.query.kernel.service.region.RegionTreeQueryService;
import io.github.eduoneproject.eduone.query.kernel.service.region.assembler.RegionTreeAssembler;
import io.github.eduoneproject.eduone.query.kernel.service.region.converter.RegionTreeConverter;
import io.github.eduoneproject.eduone.query.kernel.service.region.dto.RegionTreeModel;
import io.github.eduoneproject.eduone.query.kernel.service.region.param.RegionTreeKParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地区树服务接口实现类
 *
 * @author summerain0
 */
@RequiredArgsConstructor
@Service
public class RegionTreeQueryServiceImpl implements RegionTreeQueryService {
    private final RegionTreeQueryRepository regionTreeQueryRepository;

    @Override
    public List<RegionTreeModel> getRegionTreeList(RegionTreeKParam param) {
        RegionTreeRParam rParam = RegionTreeConverter.convert(param);
        List<RegionTreeDto> dtoList = regionTreeQueryRepository.selectList(rParam);
        return CommonConverter.convert(dtoList, RegionTreeAssembler::assemble);
    }
}
