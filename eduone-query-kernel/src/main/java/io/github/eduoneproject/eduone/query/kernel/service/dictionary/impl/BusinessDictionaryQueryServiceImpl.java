package io.github.eduoneproject.eduone.query.kernel.service.dictionary.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.eduoneproject.eduone.common.domain.DataPage;
import io.github.eduoneproject.eduone.query.common.converter.CommonConverter;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.BusinessDictionaryQueryRepository;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.dto.BusinessDictionaryDto;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.dto.BusinessDictionaryTypeDto;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryRParam;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionarySortParam;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryTypeRParam;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryTypeSortParam;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.BusinessDictionaryQueryService;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.assembler.BusinessDictionaryAssembler;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.converter.BusinessDictionaryConverter;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.model.BusinessDictionaryModel;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.model.BusinessDictionaryTypeModel;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.param.BusinessDictionaryKParam;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.param.BusinessDictionaryTypeKParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务字典查询接口实现类
 *
 * @author summerain0
 */
@Service
@RequiredArgsConstructor
public class BusinessDictionaryQueryServiceImpl implements BusinessDictionaryQueryService {
    private final BusinessDictionaryQueryRepository businessDictionaryQueryRepository;

    @Override
    public List<BusinessDictionaryModel> listBusinessDictionary(BusinessDictionaryKParam param, List<BusinessDictionarySortParam> sortList) {
        BusinessDictionaryRParam rParam = BusinessDictionaryConverter.convert(param);
        List<BusinessDictionaryDto> dtoList = businessDictionaryQueryRepository.selectList(rParam, sortList);
        return CommonConverter.convert(dtoList, BusinessDictionaryAssembler::assemble);
    }

    @Override
    public DataPage<BusinessDictionaryModel> pageBusinessDictionary(DataPage<?> page, BusinessDictionaryKParam param, List<BusinessDictionarySortParam> sortList) {
        IPage<BusinessDictionaryDto> convertPage = CommonConverter.convert2IPage(page);
        BusinessDictionaryRParam rParam = BusinessDictionaryConverter.convert(param);
        Page<BusinessDictionaryDto> dtoPage = businessDictionaryQueryRepository.selectList(convertPage, rParam, sortList);
        return CommonConverter.convert(dtoPage, BusinessDictionaryAssembler::assemble);
    }

    @Override
    public List<BusinessDictionaryTypeModel> listBusinessDictionaryType(BusinessDictionaryTypeKParam param, List<BusinessDictionaryTypeSortParam> sortList) {
        BusinessDictionaryTypeRParam rParam = BusinessDictionaryConverter.convert(param);
        List<BusinessDictionaryTypeDto> dtoList = businessDictionaryQueryRepository.selectTypeList(rParam, sortList);
        return CommonConverter.convert(dtoList, BusinessDictionaryAssembler::assemble);
    }

    @Override
    public DataPage<BusinessDictionaryTypeModel> pageBusinessDictionaryType(DataPage<?> page, BusinessDictionaryTypeKParam param, List<BusinessDictionaryTypeSortParam> sortList) {
        IPage<BusinessDictionaryTypeDto> convertPage = CommonConverter.convert2IPage(page);
        BusinessDictionaryTypeRParam rParam = BusinessDictionaryConverter.convert(param);
        Page<BusinessDictionaryTypeDto> dtoPage = businessDictionaryQueryRepository.selectTypeList(convertPage, rParam, sortList);
        return CommonConverter.convert(dtoPage, BusinessDictionaryAssembler::assemble);
    }
}
