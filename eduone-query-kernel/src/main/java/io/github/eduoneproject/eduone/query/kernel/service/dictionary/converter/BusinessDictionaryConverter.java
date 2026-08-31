package io.github.eduoneproject.eduone.query.kernel.service.dictionary.converter;

import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryRParam;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryTypeRParam;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.param.BusinessDictionaryKParam;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.param.BusinessDictionaryTypeKParam;

/**
 * 业务字典模型转换器
 *
 * @author summerain0
 */
public class BusinessDictionaryConverter {
    public static BusinessDictionaryRParam convert(BusinessDictionaryKParam kParam) {
        if (kParam == null) {
            return new BusinessDictionaryRParam();
        }
        BusinessDictionaryRParam rParam = new BusinessDictionaryRParam();
        rParam.setIdList(kParam.getIdList());
        rParam.setType(kParam.getType());
        rParam.setCodeList(kParam.getCodeList());
        return rParam;
    }

    public static BusinessDictionaryTypeRParam convert(BusinessDictionaryTypeKParam kParam) {
        if (kParam == null) {
            return new BusinessDictionaryTypeRParam();
        }
        BusinessDictionaryTypeRParam rParam = new BusinessDictionaryTypeRParam();
        rParam.setName(kParam.getName());
        return rParam;
    }
}
