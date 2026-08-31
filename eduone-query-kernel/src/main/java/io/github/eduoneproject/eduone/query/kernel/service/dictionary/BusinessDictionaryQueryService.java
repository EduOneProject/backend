package io.github.eduoneproject.eduone.query.kernel.service.dictionary;

import io.github.eduoneproject.eduone.common.domain.DataPage;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionarySortParam;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryTypeSortParam;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.model.BusinessDictionaryModel;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.model.BusinessDictionaryTypeModel;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.param.BusinessDictionaryKParam;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.param.BusinessDictionaryTypeKParam;

import java.util.List;

/**
 * 业务字典查询接口
 *
 * @author summerain0
 */
public interface BusinessDictionaryQueryService {
    /**
     * 查询业务字典
     *
     * @param param    查询参数
     * @param sortList 排序参数
     * @return 业务字典列表
     */
    List<BusinessDictionaryModel> listBusinessDictionary(BusinessDictionaryKParam param, List<BusinessDictionarySortParam> sortList);

    /**
     * 分页查询业务字典
     *
     * @param page     分页参数
     * @param param    查询参数
     * @param sortList 排序参数
     * @return 业务字典列表
     */
    DataPage<BusinessDictionaryModel> pageBusinessDictionary(DataPage<?> page, BusinessDictionaryKParam param, List<BusinessDictionarySortParam> sortList);

    /**
     * 查询业务字典类型
     *
     * @param param    查询参数
     * @param sortList 排序参数
     * @return 业务字典类型列表
     */
    List<BusinessDictionaryTypeModel> listBusinessDictionaryType(BusinessDictionaryTypeKParam param, List<BusinessDictionaryTypeSortParam> sortList);

    /**
     * 分页查询业务字典类型
     *
     * @param page     分页参数
     * @param param    查询参数
     * @param sortList 排序参数
     * @return 业务字典类型列表
     */
    DataPage<BusinessDictionaryTypeModel> pageBusinessDictionaryType(DataPage<?> page, BusinessDictionaryTypeKParam param, List<BusinessDictionaryTypeSortParam> sortList);
}
