package com.depromeet.auth.service;

import org.springframework.stereotype.Service;

import com.depromeet.auth.dto.TokenResponse;
import com.depromeet.auth.jwt.JwtService;
import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
	private final JwtService jwtService;
	private final RedisService redisService;
	private final UserRepository userRepository;

	public TokenResponse reissueToken(String refreshToken) throws IllegalAccessException {
		// Refresh Token 검증
		if (!jwtService.isValidToken(refreshToken)) {
			throw new IllegalAccessException();
		}

		Long userId = jwtService.getUserIdFromToken(refreshToken);
		User user = userRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(Result.FAIL));

		// Redis에서 저장된 Refresh Token 값을 가져옴
		String redisRefreshToken = redisService.getValues(String.valueOf(user.getUserId()));
		if (!redisRefreshToken.equals(refreshToken)) {
			throw new IllegalAccessException();
		}
		// 토큰 재발행
		String newAccessToken = jwtService.createAccessToken(user.getUserId());
		String newRefreshToken = jwtService.createRefreshToken(user.getUserId());

		return new TokenResponse(newAccessToken, newRefreshToken);
	}
}
