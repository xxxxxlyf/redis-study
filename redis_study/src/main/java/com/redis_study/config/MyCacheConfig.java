package com.redis_study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

@Configuration
@EnableScheduling
public class MyCacheConfig {


    @Bean
    public RedisCacheManager redisCache(RedisConnectionFactory connectionFactory){

        RedisCacheConfiguration configuration=RedisCacheConfiguration.defaultCacheConfig();
        configuration
                //設置緩存的默認時間為30M
                .entryTtl(Duration.ofMinutes(30L))
                //如果是空值時不緩存
                .disableCachingNullValues()
                //設置key序列化器
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                //設置value序列化器
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()));


        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
                .cacheDefaults(configuration)
                .build();
    }


    /**
     * 鍵序列化
     * @return
     */
    private RedisSerializer<String> keySerializer(){

        return new StringRedisSerializer();
    }

    /**
     * 值序列化
     * @return
     */
    private RedisSerializer<Object> valueSerializer(){

        return new GenericJackson2JsonRedisSerializer();
    }

}
