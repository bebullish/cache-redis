package cn.bebullish.cache.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

import javax.annotation.Resource;

import io.lettuce.core.RedisURI;

/**
 * Only works when <code>redis.enableRedisson=true</code>
 *
 * @author Marlon
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty("redis.enableRedisson")
public class RedissonConfig {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        RedisURI redisUri = RedisURI.create(redisProperties.getUri());
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%d", redisUri.getHost(), redisUri.getPort()))
                .setDatabase(redisUri.getDatabase())
                .setPassword(Optional.ofNullable(redisUri.getPassword()).map(String::valueOf).orElse(null));
        return Redisson.create(config);
    }

}
