package io.github.eduoneproject.eduone.business.common.support;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存工具
 *
 * @author summerain0
 */
@Component
@RequiredArgsConstructor
public class RedisCache {
    /**
     * Redis缓存前缀
     */
    private final String REDIS_KEY_PREFIX = "biz:";

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加缓存
     *
     * @param key   键
     * @param value 值
     */
    public <T> void setCacheObject(@NonNull String key, T value) {
        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + key, value);
    }

    /**
     * 添加缓存
     *
     * @param key      键
     * @param value    值
     * @param duration 过期时间
     */
    public <T> void setCacheObject(String key, T value, Duration duration) {
        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + key, value, duration.getSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 返回的数据
     */
    @SuppressWarnings("unchecked")
    public <T> T getCacheObject(String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return (T) operations.get(REDIS_KEY_PREFIX + key);
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 是否删除成功
     */
    public boolean deleteCacheObject(String key) {
        return redisTemplate.delete(REDIS_KEY_PREFIX + key);
    }

    /**
     * 是否存在指定缓存
     *
     * @param key 键
     * @return 是否存在
     */
    public boolean existsKey(String key) {
        return redisTemplate.hasKey(REDIS_KEY_PREFIX + key);
    }
}
