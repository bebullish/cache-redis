package cn.bebullish.cache.redis;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.NonNull;

import java.time.Duration;

/**
 * Implement default cache key prefix and support dynamic setting of cache key expiration time
 *
 * @author Marlon
 * @since 1.0.0
 */
public class TtlRedisCacheManager extends RedisCacheManager {

    public TtlRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    @NonNull
    protected RedisCache createRedisCache(@NonNull String name, RedisCacheConfiguration cacheConfig) {
        String[] cacheName = name.split("#");
        if (cacheName.length > 2) {
            throw new IllegalArgumentException("Invalid cacheNames, please refer to the key#seconds format");
        }
        name = cacheName[0] + ":";
        if (cacheName.length == 2) {
            long ttl = Long.parseLong(cacheName[1]);
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(ttl));
        }
        return super.createRedisCache(name, cacheConfig);

    }
}
