package io.github.eduoneproject.eduone.query.gateway.converter;

import io.github.eduoneproject.eduone.query.gateway.request.dictionary.BusinessDictionaryRequest;
import io.github.eduoneproject.eduone.query.gateway.request.dictionary.BusinessDictionarySortRequest;
import io.github.eduoneproject.eduone.query.gateway.request.dictionary.BusinessDictionaryTypeRequest;
import io.github.eduoneproject.eduone.query.gateway.request.dictionary.BusinessDictionaryTypeSortRequest;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionarySortParam;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.param.BusinessDictionaryTypeSortParam;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.param.BusinessDictionaryKParam;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.param.BusinessDictionaryTypeKParam;

/**
 * 业务字典模型转换器
 *
 * @author summerain0
 */
public class BusinessDictionaryGatewayConverter {
    public static BusinessDictionaryKParam convert(BusinessDictionaryRequest request) {
        if (request == null) {
            return new BusinessDictionaryKParam();
        }
        BusinessDictionaryKParam param = new BusinessDictionaryKParam();
        param.setIdList(request.getIdList());
        param.setType(request.getType());
        param.setParentId(request.getParentId());
        param.setCodeList(request.getCodeList());
        return param;
    }

    public static BusinessDictionarySortParam convert(BusinessDictionarySortRequest request) {
        if (request == null) {
            return null;
        }
        BusinessDictionarySortParam param = new BusinessDictionarySortParam();
        param.setField(request.getField());
        param.setDirection(request.getDirection());
        return param;
    }

    public static BusinessDictionaryTypeKParam convert(BusinessDictionaryTypeRequest request) {
        if (request == null) {
            return new BusinessDictionaryTypeKParam();
        }
        BusinessDictionaryTypeKParam param = new BusinessDictionaryTypeKParam();
        param.setName(request.getName());
        return param;
    }

    public static BusinessDictionaryTypeSortParam convert(BusinessDictionaryTypeSortRequest request) {
        if (request == null) {
            return null;
        }
        BusinessDictionaryTypeSortParam param = new BusinessDictionaryTypeSortParam();
        param.setField(request.getField());
        param.setDirection(request.getDirection());
        return param;
    }
}
