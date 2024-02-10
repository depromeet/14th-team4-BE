package com.depromeet.auth.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.auth.controller.KakaoTokenClient;
import com.depromeet.auth.controller.KakaoUserClient;
import com.depromeet.auth.dto.KakaoTokenResponse;
import com.depromeet.auth.dto.KakaoUserInfo;
import com.depromeet.auth.dto.TokenResponse;
import com.depromeet.auth.jwt.JwtService;
import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import com.depromeet.enums.Role;
import com.depromeet.enums.SocialType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

	private static final String AUTHORIZATION_TYPE = "Bearer ";
	private static final String AUTHORIZATION_CODE = "authorization_code";
	private static final String GUEST_PREFIX = "게스트";

	private final JwtService jwtService;
	private final RedisService redisService;
	private final UserRepository userRepository;
	private final KakaoTokenClient kakaoTokenClient;
	private final KakaoUserClient kakaoUserClient;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String kakaoClientId;
	@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
	private String kakaoClientSecret;
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String kakaoRedirectUrl;

	public TokenResponse kakaoLogin(String code) {
		// 액세스 토큰 요청
		KakaoTokenResponse tokenResponse = kakaoTokenClient.getToken(AUTHORIZATION_CODE, kakaoClientId, kakaoRedirectUrl, code,
			kakaoClientSecret);
		// 사용자 정보 요청
		KakaoUserInfo userResponse = kakaoUserClient.getUserInfo(AUTHORIZATION_TYPE + tokenResponse.getAccess_token());
		// 사용자 정보로 회원가입 및 로그인
		User user = userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, userResponse.getId())
			.orElseGet(() -> createUser(userResponse));
		//토큰 발급
		String accessToken = jwtService.createAccessToken(user.getUserId());
		String refreshToken = jwtService.createRefreshToken(user.getUserId());

		return new TokenResponse(accessToken, refreshToken);
	}


	public TokenResponse reissueToken(String refreshToken) {

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
		if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
			log.info(redisRefreshToken);
			throw new CustomException(Result.TOKEN_INVALID);
		}
		// 토큰 재발행
		String newAccessToken = jwtService.createAccessToken(user.getUserId());
		String newRefreshToken = jwtService.createRefreshToken(user.getUserId());

		return new TokenResponse(newAccessToken, newRefreshToken);
	}

	@Transactional
	public void logout(User user, HttpServletRequest request, HttpServletResponse response) {
		// 엑세스 토큰에서 사용자 ID 추출
		String accessToken = jwtService.resolveToken(request);

		// redis에서 삭제
		redisService.deleteValues(String.valueOf(user.getUserId()));

		// redis에 블랙리스트 등록
		Long leftAccessTokenTTlSecond = jwtService.getLeftAccessTokenTTLSecond(accessToken);
		redisService.setValues(accessToken, "logout", leftAccessTokenTTlSecond);
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

	private User createUser(KakaoUserInfo userInfo) {
		User newUser = User.builder()
			.socialType(SocialType.KAKAO)
			.socialId(userInfo.getId())
			.nickName(GUEST_PREFIX + userInfo.getId())
			.userRole(Role.GUEST)
			.build();
		return userRepository.save(newUser);
	}
}
