package io.github.eduoneproject.eduone.common.util;

import java.util.UUID;

/**
 * id工具类
 *
 * @author summerain0
 */
public class IdUtils {
    /**
     * 获取uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取简单uuid
     */
    public static String simpleUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
