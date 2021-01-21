package cn.bebullish.cache.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * Only works when <code>redis.enableRedisson=true</code>
 *
 * @author Marlon
 * @since 1.0.0
 */
@Slf4j
@Component
@ConditionalOnProperty("redis.enableRedisson")
public class RedissonService {

    @Resource
    private RedissonClient redissonClient;

    public void runWithLock(String lockKey, long waitTime, long releaseTime, Runnable runnable) {
        Assert.hasLength(lockKey, "lockKey must not be null or empty!");
        RLock rLock = redissonClient.getLock(lockKey);
        boolean isLock = false;
        try {
            isLock = rLock.tryLock(waitTime, releaseTime, TimeUnit.MICROSECONDS);
            if (isLock) {
                runnable.run();
            }
        } catch (InterruptedException e) {
            log.error("[redisson] InterruptedException");
            Thread.currentThread().interrupt();
        } finally {
            if (isLock && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    public void runWithLock(String lockKey, Runnable runnable) {
        Assert.hasLength(lockKey, "lockKey must not be null or empty!");
        RLock rLock = redissonClient.getLock(lockKey);
        boolean isLock = false;
        try {
            isLock = rLock.tryLock(5, 5, TimeUnit.SECONDS);
            if (isLock) {
                runnable.run();
            }
        } catch (InterruptedException e) {
            log.error("[redisson] InterruptedException");
            Thread.currentThread().interrupt();
        } finally {
            if (isLock && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

}
