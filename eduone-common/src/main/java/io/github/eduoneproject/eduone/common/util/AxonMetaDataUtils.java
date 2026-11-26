package io.github.eduoneproject.eduone.common.util;

import io.github.eduoneproject.eduone.common.consts.AxonMetadataKey;
import io.github.eduoneproject.eduone.common.domain.context.ServiceContext;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Axon元数据工具
 *
 * @author summerain0
 */
public class AxonMetaDataUtils {
    /**
     * 获取服务上下文
     *
     * @param metadata 元数据
     * @return 服务上下文
     */
    public static ServiceContext getServiceContext(Map<String, String> metadata) {
        String value = MapUtils.getString(metadata, AxonMetadataKey.SERVICE_CONTEXT);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return ServiceContext.fromJson(value);
    }
}
