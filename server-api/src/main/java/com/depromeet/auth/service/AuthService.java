package com.depromeet.auth.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		User user = userRepository.findById(userId)
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

	@Transactional
	public TokenResponse signup(User user) {
		User guestUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

		guestUser.updateUserRole();
		guestUser.updateNickname(getRandomNickname());

		// 토큰 재발행
		String accessToken = jwtService.createAccessToken(guestUser.getUserId());
		String refreshToken = jwtService.createRefreshToken(guestUser.getUserId());

		return new TokenResponse(accessToken, refreshToken);
	}

	private String getRandomNickname() {
		String nickname;
		do {
			nickname = createRandomNickname();
		} while (userRepository.existsByNickName(nickname));
		return nickname;
	}

	private String createRandomNickname() {
		String prefix = "또잇";
		int length = 6;
		String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(allowedChars.length());
			sb.append(allowedChars.charAt(randomIndex));
		}
		return prefix + sb;
	}
}
