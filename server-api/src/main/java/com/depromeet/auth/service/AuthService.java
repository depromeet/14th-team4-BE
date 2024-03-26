package com.depromeet.auth.service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.auth.apple.AppleAuthClient;
import com.depromeet.auth.controller.KakaoTokenClient;
import com.depromeet.auth.controller.KakaoUserClient;
import com.depromeet.auth.dto.ApplePublicKeyResponse;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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
	private final AppleAuthClient appleAuthClient;
	private final KakaoTokenClient kakaoTokenClient;
	private final KakaoUserClient kakaoUserClient;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String kakaoClientId;
	@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
	private String kakaoClientSecret;

	public TokenResponse kakaoLogin(String code, String kakaoRedirectUrl) {
		validateUrlParam(kakaoRedirectUrl);

		// 액세스 토큰 요청
		KakaoTokenResponse tokenResponse = kakaoTokenClient.getToken(AUTHORIZATION_CODE, kakaoClientId,
			kakaoRedirectUrl, code,
			kakaoClientSecret);
		// 사용자 정보 요청
		KakaoUserInfo userResponse = kakaoUserClient.getUserInfo(AUTHORIZATION_TYPE + tokenResponse.getAccess_token());
		// 사용자 정보로 회원가입 및 로그인
		User user = userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, userResponse.getId())
			.orElseGet(() -> createUser(SocialType.KAKAO, userResponse.getId()));

		boolean isFirst = user.getUserRole().equals(Role.GUEST);
		//토큰 발급
		String accessToken = jwtService.createAccessToken(user.getUserId());
		String refreshToken = jwtService.createRefreshToken(user.getUserId());

		return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).isFirst(isFirst).build();
	}

	private void validateUrlParam(String kakaoRedirectUrl) {
		if (Objects.isNull(kakaoRedirectUrl)) {
			throw new CustomException(Result.BAD_REQUEST);
		}
	}

	public TokenResponse appleLogin(String idToken) {

		Claims appleClaims = getClaimsBy(idToken);
		String appleEmail = (String)appleClaims.get("email");

		log.info("AppleInfo by idToken after parsing Claim >>>> " + appleEmail);

		User user = userRepository.findBySocialTypeAndSocialId(SocialType.APPLE, appleEmail)
			.orElseGet(() -> createUser(SocialType.APPLE, appleEmail));

		boolean isFirst = user.getUserRole().equals(Role.GUEST);

		//토큰 발급
		String accessToken = jwtService.createAccessToken(user.getUserId());
		String refreshToken = jwtService.createRefreshToken(user.getUserId());

		return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).isFirst(isFirst).build();
	}

	public TokenResponse reissueToken(String refreshToken) {
		log.info("reissueToken : " + refreshToken);
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

		return TokenResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build();
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
		User guestUser = userRepository.findById(user.getUserId())
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

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

	private User createUser(SocialType socialType, String socialId) {
		String randomUUID = UUID.randomUUID().toString().toUpperCase().substring(0, 7);

		User newUser = User.builder()
			.socialType(socialType)
			.socialId(socialId)
			.nickname(GUEST_PREFIX + randomUUID)
			.userRole(Role.GUEST)
			.build();
		return userRepository.save(newUser);
	}

	/**
	 * Verify the Identity Token
	 * 애플 서버의 public key를 사용해 JWS E256 signature를 검증(identity Token내 payload에 속한 값들이 변조되지 않았는지 검증위해)
	 */
	public Claims getClaimsBy(String idToken) {
		try {
			// identityToken 서명 검증용 publicKey 요청
			ApplePublicKeyResponse response = appleAuthClient.getAppleAuthPublicKey();

			String headerOfIdentityToken = idToken.substring(0, idToken.indexOf("."));
			Map<String, String> header = new ObjectMapper().readValue(
				new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class);
			ApplePublicKeyResponse.ApplePublicKey key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
				.orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

			byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
			byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

			BigInteger n = new BigInteger(1, nBytes);
			BigInteger e = new BigInteger(1, eBytes);

			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
			KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

			return getAppleClaims(publicKey, idToken);

		} catch (Exception e) {
			throw new CustomException(Result.TOKEN_INVALID);
		}
	}

	private Claims getAppleClaims(PublicKey publicKey, String identityToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(publicKey)
				.build()
				.parseClaimsJws(identityToken)
				.getBody();    // email 등 중요 요소 사용하면 된다.
		} catch (JwtException e) {
			throw new CustomException(Result.TOKEN_INVALID);
		}
	}
}
