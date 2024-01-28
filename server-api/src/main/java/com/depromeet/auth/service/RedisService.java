package com.depromeet.auth.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisService {
	private final StringRedisTemplate redisTemplate;

	/*
	데이터 저장 (key-value)
	 */
	public void setValues(String key, String data) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(key, data);
	}

	/*
	만료시간 설정 데이터 저장
	 */
	public void setValues(String key, String data, Long expiredTime) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(key, data, Duration.ofMillis(expiredTime));
	}

	/*
	데이터 조회
	 */
	@Transactional(readOnly = true)
	public String getValues(String key) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		return values.get(key); // 키가 존재하지 않으면 null 반환
	}

	/*
	데이터 삭제
	 */
	public void deleteValues(String key) {
		redisTemplate.delete(key);
	}
}
