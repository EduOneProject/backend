package io.github.eduoneproject.eduone.query.kernel.repository.dictionary;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.dto.BusinessDictionaryDto;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.dto.BusinessDictionaryTypeDto;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryRParam;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionarySortParam;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryTypeRParam;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryTypeSortParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 业务字典查询仓储层
 *
 * @author summerain0
 */
@Mapper
public interface BusinessDictionaryQueryRepository {
    /**
     * 查询业务字典
     *
     * @param param    查询条件
     * @param sortList 排序条件
     * @return 业务字典列表
     */
    List<BusinessDictionaryDto> selectList(BusinessDictionaryRParam param, List<BusinessDictionarySortParam> sortList);

    /**
     * 分页获取业务字典列表
     *
     * @param page     分页参数
     * @param param    查询条件
     * @param sortList 排序条件
     * @return 业务字典分页列表
     */
    Page<BusinessDictionaryDto> selectList(IPage<BusinessDictionaryDto> page, BusinessDictionaryRParam param, List<BusinessDictionarySortParam> sortList);

    /**
     * 查询业务字典类型
     *
     * @param param    查询条件
     * @param sortList 排序条件
     * @return 业务字典类型列表
     */
    List<BusinessDictionaryTypeDto> selectTypeList(BusinessDictionaryTypeRParam param, List<BusinessDictionaryTypeSortParam> sortList);

    /**
     * 分页获取业务字典类型列表
     *
     * @param page     分页参数
     * @param param    查询条件
     * @param sortList 排序条件
     * @return 业务字典类型分页列表
     */
    Page<BusinessDictionaryTypeDto> selectTypeList(IPage<BusinessDictionaryTypeDto> page, BusinessDictionaryTypeRParam param, List<BusinessDictionaryTypeSortParam> sortList);
}
