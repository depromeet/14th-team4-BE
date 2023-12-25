package com.depromeet.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {
	private final RedisProperties redisProperties;

	// RedisProperties로 yaml에 저장한 host, post를 연결
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		System.out.println(redisProperties.getHost());
		System.out.println(redisProperties.getPort());
		return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
	}

	// serializer 설정으로 redis-cli를 통해 직접 데이터를 조회할 수 있도록 설정
	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String > redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(redisConnectionFactory());

		return redisTemplate;
	}
}
