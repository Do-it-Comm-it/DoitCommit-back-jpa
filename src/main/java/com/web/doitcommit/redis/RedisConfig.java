package com.web.doitcommit.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableRedisRepositories
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host,port);
    }

    @Bean
    public RedisTemplate<byte[],byte[]> redisTemplate() {
        RedisTemplate<byte[],byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // 아래 두 라인을 작성하지 않으면, key값이 \xac\xed\x00\x05t\x00\x03sol 이렇게 조회된다.
        //redisTemplate.setKeySerializer();
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
