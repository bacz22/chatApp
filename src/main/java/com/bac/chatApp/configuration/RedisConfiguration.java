package com.bac.chatApp.configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    // @Bean
    // public ObjectMapper redisObjectMapper() {
    //     ObjectMapper mapper = new ObjectMapper();
        
    //     mapper.registerModule(new JavaTimeModule());
        
    //     mapper.activateDefaultTyping(
    //         BasicPolymorphicTypeValidator.builder()
    //             .allowIfSubType(Object.class)
    //             .build(),
    //         ObjectMapper.DefaultTyping.NON_FINAL,
    //         JsonTypeInfo.As.PROPERTY
    //     );
        
    //     return mapper;
    // }

    // @Bean
    // public RedisTemplate<String, Object> redisTemplate(
    //         LettuceConnectionFactory connectionFactory,
    //         ObjectMapper redisObjectMapper) {

    //     RedisTemplate<String, Object> template = new RedisTemplate<>();
    //     template.setConnectionFactory(connectionFactory);

    //     // String serializer cho keys
    //     StringRedisSerializer stringSerializer = new StringRedisSerializer();
        
    //     // JSON serializer với custom ObjectMapper
    //     GenericJackson2JsonRedisSerializer jsonSerializer = 
    //         new GenericJackson2JsonRedisSerializer(redisObjectMapper);

    //     // Key serialization
    //     template.setKeySerializer(stringSerializer);
    //     template.setHashKeySerializer(stringSerializer);

    //     // Value serialization
    //     template.setValueSerializer(jsonSerializer);
    //     template.setHashValueSerializer(jsonSerializer);

    //     // Enable transaction support (optional)
    //     template.setEnableTransactionSupport(false);

    //     template.afterPropertiesSet();
    //     return template;
    // }
}
