package io.github.eduoneproject.eduone.query.kernel.service.dictionary.assembler;

import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.dto.BusinessDictionaryDto;
import io.github.eduoneproject.eduone.query.kernel.repository.dictionary.dto.BusinessDictionaryTypeDto;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.model.BusinessDictionaryModel;
import io.github.eduoneproject.eduone.query.kernel.service.dictionary.model.BusinessDictionaryTypeModel;

/**
 * 业务字典模型组装器
 *
 * @author summerain0
 */
public class BusinessDictionaryAssembler {
    public static BusinessDictionaryModel assemble(BusinessDictionaryDto dto) {
        if (dto == null) {
            return null;
        }
        BusinessDictionaryModel model = new BusinessDictionaryModel();
        model.setId(dto.getId());
        model.setType(dto.getType());
        model.setCode(dto.getCode());
        model.setName(dto.getName());
        model.setParentId(dto.getParentId());
        model.setStatus(dto.getStatus());
        model.setSort(dto.getSort());
        model.setUpdateUserId(dto.getUpdateUserId());
        model.setUpdateTime(dto.getUpdateTime());
        model.setCreateUserId(dto.getCreateUserId());
        model.setCreateTime(dto.getCreateTime());
        return model;
    }

    public static BusinessDictionaryTypeModel assemble(BusinessDictionaryTypeDto dto) {
        if (dto == null) {
            return null;
        }
        BusinessDictionaryTypeModel model = new BusinessDictionaryTypeModel();
        model.setType(dto.getType());
        model.setName(dto.getName());
        model.setDesc(dto.getDesc());
        model.setUpdateUserId(dto.getUpdateUserId());
        model.setUpdateTime(dto.getUpdateTime());
        model.setCreateUserId(dto.getCreateUserId());
        model.setCreateTime(dto.getCreateTime());
        return model;
    }
}
