# cache-redis

## 仓库说明
自身打为 Jar 包为 SpringBoot 项目提供 redis 及 redisson 相关配置

## 如何引用

### 添加依赖
```groovy
repositories {
    maven {
        url 'https://marlon-maven.pkg.coding.net/repository/artifact/public/'
    }
}

dependencies {
    implementation 'cn.bebullish:cache-redis:1.0.0'
}
```

### 使配置生效
```java
@SpringBootApplication
@EnableRedis
```

### 变量配置

[RedisProperties](https://github.com/bebullish/cache-redis/blob/master/src/main/java/cn/bebullish/cache/redis/RedisProperties.java)