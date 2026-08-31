package io.github.eduoneproject.eduone.query.gateway.resolver;

import io.github.eduoneproject.eduone.common.domain.DataPage;
import io.github.eduoneproject.eduone.query.common.converter.CommonConverter;
import io.github.eduoneproject.eduone.query.gateway.assembler.BusinessDictionaryGatewayAssembler;
import io.github.eduoneproject.eduone.query.gateway.converter.BusinessDictionaryGatewayConverter;
import io.github.eduoneproject.eduone.query.gateway.converter.CommonQueryGatewayConverter;
import io.github.eduoneproject.eduone.query.gateway.request.PageRequest;
import io.github.eduoneproject.eduone.query.gateway.request.dictionary.BusinessDictionaryRequest;
import io.github.eduoneproject.eduone.query.gateway.request.dictionary.BusinessDictionarySortRequest;
import io.github.eduoneproject.eduone.query.gateway.request.dictionary.BusinessDictionaryTypeRequest;
import io.github.eduoneproject.eduone.query.gateway.request.dictionary.BusinessDictionaryTypeSortRequest;
import io.github.eduoneproject.eduone.query.gateway.response.dictionary.BusinessDictionaryResponse;
import io.github.eduoneproject.eduone.query.gateway.response.dictionary.BusinessDictionaryTypeResponse;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionarySortParam;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryTypeSortParam;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.BusinessDictionaryQueryService;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.model.BusinessDictionaryModel;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.model.BusinessDictionaryTypeModel;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.param.BusinessDictionaryKParam;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.param.BusinessDictionaryTypeKParam;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 业务字典查询接口
 *
 * @author summerain0
 */
@Controller
@RequiredArgsConstructor
public class BusinessDictionaryQueryResolver {
    private final BusinessDictionaryQueryService businessDictionaryQueryService;

    /**
     * 查询业务字典
     *
     * @param request  查询参数
     * @param sortList 排序参数
     * @return 业务字典列表
     */
    @QueryMapping
    public List<BusinessDictionaryResponse> listBusinessDictionary(@Argument BusinessDictionaryRequest request, @Argument List<BusinessDictionarySortRequest> sortList) {
        BusinessDictionaryKParam param = BusinessDictionaryGatewayConverter.convert(request);
        List<BusinessDictionarySortParam> sortParamList = CommonConverter.convert(sortList, BusinessDictionaryGatewayConverter::convert);
        List<BusinessDictionaryModel> modelList = businessDictionaryQueryService.listBusinessDictionary(param, sortParamList);
        return CommonConverter.convert(modelList, BusinessDictionaryGatewayAssembler::assemble);
    }

    /**
     * 分页查询业务字典
     *
     * @param page     分页参数
     * @param request  查询参数
     * @param sortList 排序参数
     * @return 业务字典列表
     */
    @QueryMapping
    public DataPage<BusinessDictionaryResponse> pageBusinessDictionary(@Argument PageRequest page, @Argument BusinessDictionaryRequest request, @Argument List<BusinessDictionarySortRequest> sortList) {
        DataPage<BusinessDictionaryModel> convertPage = CommonConverter.convert(CommonQueryGatewayConverter.convert(page));
        BusinessDictionaryKParam param = BusinessDictionaryGatewayConverter.convert(request);
        List<BusinessDictionarySortParam> sortParamList = CommonConverter.convert(sortList, BusinessDictionaryGatewayConverter::convert);
        DataPage<BusinessDictionaryModel> dtoPage = businessDictionaryQueryService.pageBusinessDictionary(convertPage, param, sortParamList);
        return CommonConverter.convert(dtoPage, BusinessDictionaryGatewayAssembler::assemble);
    }

    /**
     * 查询业务字典类型
     *
     * @param request  查询参数
     * @param sortList 排序参数
     * @return 业务字典类型列表
     */
    @QueryMapping
    public List<BusinessDictionaryTypeResponse> listBusinessDictionaryType(@Argument BusinessDictionaryTypeRequest request, @Argument List<BusinessDictionaryTypeSortRequest> sortList) {
        BusinessDictionaryTypeKParam param = BusinessDictionaryGatewayConverter.convert(request);
        List<BusinessDictionaryTypeSortParam> sortParamList = CommonConverter.convert(sortList, BusinessDictionaryGatewayConverter::convert);
        List<BusinessDictionaryTypeModel> modelList = businessDictionaryQueryService.listBusinessDictionaryType(param, sortParamList);
        return CommonConverter.convert(modelList, BusinessDictionaryGatewayAssembler::assemble);
    }

    /**
     * 分页查询业务字典类型
     *
     * @param page     分页参数
     * @param request  查询参数
     * @param sortList 排序参数
     * @return 业务字典类型列表
     */
    @QueryMapping
    public DataPage<BusinessDictionaryTypeResponse> pageBusinessDictionaryType(@Argument PageRequest page, @Argument BusinessDictionaryTypeRequest request, @Argument List<BusinessDictionaryTypeSortRequest> sortList) {
        DataPage<BusinessDictionaryTypeModel> convertPage = CommonConverter.convert(CommonQueryGatewayConverter.convert(page));
        BusinessDictionaryTypeKParam param = BusinessDictionaryGatewayConverter.convert(request);
        List<BusinessDictionaryTypeSortParam> sortParamList = CommonConverter.convert(sortList, BusinessDictionaryGatewayConverter::convert);
        DataPage<BusinessDictionaryTypeModel> dtoPage = businessDictionaryQueryService.pageBusinessDictionaryType(convertPage, param, sortParamList);
        return CommonConverter.convert(dtoPage, BusinessDictionaryGatewayAssembler::assemble);
    }
}
