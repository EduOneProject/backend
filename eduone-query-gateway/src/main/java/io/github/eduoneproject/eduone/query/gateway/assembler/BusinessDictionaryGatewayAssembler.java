package io.github.eduoneproject.eduone.query.gateway.assembler;

import io.github.eduoneproject.eduone.query.gateway.response.dictionary.BusinessDictionaryResponse;
import io.github.eduoneproject.eduone.query.gateway.response.dictionary.BusinessDictionaryTypeResponse;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.model.BusinessDictionaryModel;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.model.BusinessDictionaryTypeModel;

/**
 * 业务字典模型组装器
 *
 * @author summerain0
 */
public class BusinessDictionaryGatewayAssembler {
    public static BusinessDictionaryResponse assemble(BusinessDictionaryModel model) {
        if (model == null) {
            return null;
        }
        BusinessDictionaryResponse response = new BusinessDictionaryResponse();
        response.setId(model.getId());
        response.setType(model.getType());
        response.setCode(model.getCode());
        response.setName(model.getName());
        response.setParentId(model.getParentId());
        response.setStatus(model.getStatus());
        response.setSort(model.getSort());
        response.setUpdateUserId(model.getUpdateUserId());
        response.setUpdateTime(model.getUpdateTime());
        response.setCreateUserId(model.getCreateUserId());
        response.setCreateTime(model.getCreateTime());
        return response;
    }

    public static BusinessDictionaryTypeResponse assemble(BusinessDictionaryTypeModel model) {
        if (model == null) {
            return null;
        }
        BusinessDictionaryTypeResponse response = new BusinessDictionaryTypeResponse();
        response.setType(model.getType());
        response.setName(model.getName());
        response.setDesc(model.getDesc());
        response.setUpdateUserId(model.getUpdateUserId());
        response.setUpdateTime(model.getUpdateTime());
        response.setCreateUserId(model.getCreateUserId());
        response.setCreateTime(model.getCreateTime());
        return response;
    }
}
