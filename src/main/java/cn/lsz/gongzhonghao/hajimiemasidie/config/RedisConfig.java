package cn.lsz.gongzhonghao.hajimiemasidie.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class RedisConfig {

    /**
     * 修改RedisTemplate 序列化方式（如果不需要看缓存数据可以不改）
     */
    @Bean("redisTemplate")
    public RedisTemplate<String,Object> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //开启事务
        //template.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 由于菜单那里用了数组，GenericJackson2JsonRedisSerializer序列化有问题，多配置了一个原生的序列化方式RedisTemplate
     */
    @Bean("sourceRedisTemplate")
    public RedisTemplate sourceRedisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //开启事务
        //template.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

}
