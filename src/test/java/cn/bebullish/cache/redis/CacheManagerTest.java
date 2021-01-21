package cn.bebullish.cache.redis;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheManagerTest {

    @Cacheable(cacheNames = "TEST#120", key = "#key")
    public String cacheable(String key) {
        return key;
    }

    @Cacheable(cacheNames = "TEST:DEFAULT:EXPIRE", key = "#key")
    public String cacheableDefaultExpire(String key) {
        return key;
    }

    @CacheEvict(cacheNames = "TEST", key = "#key")
    public void evict(String key) {
    }

}
