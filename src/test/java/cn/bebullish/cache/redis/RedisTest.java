package cn.bebullish.cache.redis;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import lombok.SneakyThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootApplication
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RedisTest {

    public static void main(String[] args) {
        SpringApplication.run(RedisTest.class, args);
    }

    @Resource
    private CacheManagerTest cacheManagerTest;
    @Resource
    private ValueOperations<String, Object> valueOperations;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private RedisProperties redisProperties;
    @Resource
    private ObjectMapper redisObjectMapper;

    @Test
    public void mainTest() {
        testCacheManagerPrefix();
        testCacheManagerExpire();
        testSerializable();
        testExpire();
    }

    private void testExpire() {
        valueOperations.set("key", "2");
        assertEquals(redisTemplate.getExpire("key"), -1);
        assertEquals(redisTemplate.delete("key"), Boolean.TRUE);
    }

    private void testCacheManagerPrefix() {
        String testKey = "test";
        String cacheKey = getCacheKey("TEST", testKey);
        assertEquals(cacheManagerTest.cacheable(testKey), valueOperations.get(cacheKey));
        cacheManagerTest.evict(testKey);
        assertNull(valueOperations.get(cacheKey));
    }

    private void testCacheManagerExpire() {
        String testKey = "test";
        String cacheKey = getCacheKey("TEST", testKey);
        cacheManagerTest.cacheable(testKey);
        assertEquals(redisTemplate.getExpire(cacheKey), 120);
        cacheManagerTest.evict(testKey);
        assertNull(valueOperations.get(cacheKey));

        assertEquals(cacheManagerTest.cacheableDefaultExpire(testKey), testKey);
        cacheKey = getCacheKey("TEST:DEFAULT:EXPIRE", testKey);
        assertEquals(redisTemplate.getExpire(cacheKey), redisProperties.getTtl().getSeconds());
        redisTemplate.delete(cacheKey);
        assertNull(valueOperations.get(cacheKey));
    }

    @SneakyThrows
    private void testSerializable() {
        Time time1 = new Time();
        valueOperations.set("time", time1);
        Time time2 = (Time) valueOperations.get("time");
        assertEquals(redisObjectMapper.writeValueAsString(time1), redisObjectMapper.writeValueAsString(time2));
        assertEquals(redisTemplate.delete("time"), Boolean.TRUE);
    }

    private String getCacheKey(String cacheNamesSuffix, String cacheKey) {
        return String.join(":", redisProperties.getCacheNamePrefix(), cacheNamesSuffix, cacheKey);
    }

}
