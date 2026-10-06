package io.github.eduoneproject.eduone.query.gateway.resolver;

import io.github.eduoneproject.eduone.common.annotation.OptionalLogin;
import io.github.eduoneproject.eduone.query.common.converter.CommonConverter;
import io.github.eduoneproject.eduone.query.gateway.assembler.RegionTreeGatewayAssembler;
import io.github.eduoneproject.eduone.query.gateway.converter.RegionTreeGatewayConverter;
import io.github.eduoneproject.eduone.query.gateway.request.RegionTreeQueryRequest;
import io.github.eduoneproject.eduone.query.gateway.response.RegionTreeResponse;
import io.github.eduoneproject.eduone.query.kernel.service.region.RegionTreeQueryService;
import io.github.eduoneproject.eduone.query.kernel.service.region.model.RegionTreeModel;
import io.github.eduoneproject.eduone.query.kernel.service.region.param.RegionTreeKParam;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 地区树查询接口
 *
 * @author summerain0
 */
@Controller
@RequiredArgsConstructor
public class RegionTreeQueryResolver {
    private final RegionTreeQueryService regionTreeQueryService;

    /**
     * 查询地区树信息
     *
     * @param request 查询参数
     * @return 地区树
     */
    @QueryMapping
    @OptionalLogin
    public List<RegionTreeResponse> listRegionTree(@Argument RegionTreeQueryRequest request) {
        RegionTreeKParam kParam = RegionTreeGatewayConverter.convert(request);
        List<RegionTreeModel> modelList = regionTreeQueryService.getRegionTreeList(kParam);
        return CommonConverter.convert(modelList, RegionTreeGatewayAssembler::assemble);
    }
}
