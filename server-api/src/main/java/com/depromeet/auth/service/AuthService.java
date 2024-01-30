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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
	private final JwtService jwtService;
	private final RedisService redisService;
	private final CookieService cookieService;
	private final UserRepository userRepository;

	public TokenResponse reissueToken(HttpServletRequest request) throws IllegalAccessException {
		// get cookie
		Cookie cookie = cookieService.getCookie(request, "refreshToken").orElseThrow();
		String refreshToken = cookie.getValue();

		// Refresh Token 검증
		if (!jwtService.isValidToken(refreshToken)) {
			throw new CustomException(Result.TOKEN_INVALID);
		}

		Long userId = jwtService.getUserIdFromToken(refreshToken);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

		if (user.getDeletedAt() != null) {
			throw new CustomException(Result.DELETED_USER);
		}

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
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		// 1. 엑세스 토큰에서 사용자 ID 추출
		String accessToken = jwtService.resolveToken(request);
		Long userId = jwtService.getUserIdFromToken(accessToken);

		// 2. 리프레시 토큰 가져오기
		Cookie cookie = cookieService.getCookie(request, "refreshToken")
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_COOKIE));
		String refreshToken = cookie.getValue();

		// 3. Redis에 저장된 리프레시 토큰과 비교
		String savedRefreshToken = redisService.getValues(String.valueOf(userId));
		log.info("savedRefreshToken : " + savedRefreshToken);
		log.info("headerRefreshToken : " + refreshToken);

		if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
			log.info("저장된 리프레쉬토큰이 없거나, 다른 토큰이 일치하지 않는 경우");
			throw new CustomException(Result.BAD_REQUEST);
		}

		// redis에서 삭제
		redisService.deleteValues(String.valueOf(userId));

		// redis에 블랙리스트 등록
		Long leftAccessTokenTTlSecond = jwtService.getLeftAccessTokenTTLSecond(accessToken);
		redisService.setValues(accessToken, "logout", leftAccessTokenTTlSecond);

		cookieService.deleteAccessTokenCookie(response);
		cookieService.deleteRefreshTokenCookie(response);
	}

	@Transactional
	public void signup(User user) {
		User guestUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

		if (guestUser.getDeletedAt() != null) {
			throw new CustomException(Result.DELETED_USER);
		}
		guestUser.updateUserRole();
		guestUser.updateNickname(getRandomNickname());
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
