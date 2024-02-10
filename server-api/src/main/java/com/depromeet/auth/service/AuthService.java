package com.depromeet.auth.service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.auth.apple.AppleAuthClient;
import com.depromeet.auth.apple.AppleIdTokenPayload;
import com.depromeet.auth.apple.AppleProperties;
import com.depromeet.auth.apple.TokenDecoder;
import com.depromeet.auth.dto.ApplePublicKeyResponse;
import com.depromeet.auth.dto.AppleToken;
import com.depromeet.auth.dto.SocialLoginRequest;
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
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
	private static final String APPLE_JWT_ALGORITHM = "ES256";
	private final JwtService jwtService;
	private final RedisService redisService;
	private final CookieService cookieService;
	private final UserRepository userRepository;
	private final AppleAuthClient appleAuthClient;
	private final AppleProperties appleProperties;

	public TokenResponse reissueToken(HttpServletRequest request) {
		Cookie cookie = cookieService.getCookie(request, "refreshToken")
			.orElseThrow(() -> new CustomException(Result.NOT_FOUND_COOKIE));
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
			throw new CustomException(Result.TOKEN_INVALID);
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

	public TokenResponse signupWithApple(String identityToken, String authorizationCode) {
		Claims appleClaims = getClaimsBy(identityToken);
		String appleUserId = appleClaims.getSubject();
		String appleEmail = (String)appleClaims.get("email");
		String appleName = (String)appleClaims.get("name");
		String appleAccountId = appleClaims.getId();

		log.error("AppleInfo by identityToken after parsing Claim >>>> " + appleUserId + " / " + appleEmail + "/"
			+ appleName + "/" + appleAccountId);

		Optional<User> findUser = userRepository.findBySocialTypeAndSocialId(SocialType.APPLE,
			appleAccountId);

		boolean isFirstValue = false;
		// 기존에 없으면 생성
		if (!findUser.isPresent()) {
			User createdUser = User.builder()
				.socialType(SocialType.APPLE)
				.socialId(appleAccountId)
				.nickName("게스트" + appleAccountId)
				.userRole(Role.GUEST)
				.build();

			isFirstValue = true;
			userRepository.save(createdUser);
		}

		// todo - authorizationCode 으로 토큰만 가져와야 하는데 email, sub 가져오는 로직 왜 다른지 확인
		AppleIdTokenPayload appleIdTokenPayload = get(authorizationCode);
		// appleIdTokenPayload.getEmail();
		// appleIdTokenPayload.getSub();

		// todo 애플에서 주는 refreshToken 으로 세션유지?
		return new TokenResponse(null, null, isFirstValue);
	}

	public TokenResponse loginWithApple(SocialLoginRequest loginRequest) {
		String clientSecret = makeAppleClientSecret();

		AppleToken.Request tokenRequest = AppleToken.Request.of(loginRequest.getRefreshToken()
			, appleProperties.getClientId(), clientSecret);

		AppleToken.Response response = appleAuthClient.generateOrValidateToken(tokenRequest);

		String accessToken = jwtService.createAccessToken(null);
		String refreshToken = jwtService.createAccessToken(null);

		return new TokenResponse(accessToken, refreshToken, true);
	}

	/**
	 *
	 * @param authorizationCode
	 * 	app 측에서 받은 authorizationCode
	 * 	1번만 사용가능, 5분 만료
	 */
	public AppleIdTokenPayload get(String authorizationCode) {

		String clientSecret = makeAppleClientSecret();

		AppleToken.Request tokenRequest = AppleToken.Request.of(authorizationCode,
			appleProperties.getClientId(),
			clientSecret, appleProperties.getGrantType());

		// 로그인에 사용될 token 요청
		AppleToken.Response tokenResponse = appleAuthClient.generateOrValidateToken(tokenRequest);
		String idToken = tokenResponse.getIdToken();

		return TokenDecoder.decodePayload(idToken, AppleIdTokenPayload.class);
	}

	/**
	 * Verify the Identity Token
	 * 애플 서버의 public key를 사용해 JWS E256 signature를 검증(identity Token내 payload에 속한 값들이 변조되지 않았는지 검증위해)
	 */
	public Claims getClaimsBy(String identityToken) {
		try {
			// identityToken 서명 검증용 publicKey 요청
			ApplePublicKeyResponse response = appleAuthClient.getAppleAuthPublicKey();

			String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));
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

			return getAppleClaims(publicKey, identityToken);

		} catch (NoSuchAlgorithmException e) {
			throw new CustomException(Result.TOKEN_INVALID);
		} catch (InvalidKeySpecException e) {
			throw new CustomException(Result.TOKEN_INVALID);
		} catch (MalformedJwtException e) {
			//토큰 서명 검증 or 구조 문제 (Invalid token)
			throw new CustomException(Result.TOKEN_INVALID);
		} catch (ExpiredJwtException e) {
			//토큰이 만료됐기 때문에 클라이언트는 토큰을 refresh 해야함.
			throw new CustomException(Result.TOKEN_INVALID);
		} catch (Exception e) {
			throw new CustomException(Result.TOKEN_INVALID);
		}
	}

	public String makeAppleClientSecret() {
		return Jwts.builder()
			.setHeaderParam(JwsHeader.KEY_ID, appleProperties.getKeyId())
			.setHeaderParam(JwsHeader.ALGORITHM, APPLE_JWT_ALGORITHM)
			.setIssuer(appleProperties.getTeamId())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			// .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
			.setExpiration(
				Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()))
			.setAudience(appleProperties.getAudience())
			.setSubject(appleProperties.getClientId())
			.signWith(getApplePrivateKey(), SignatureAlgorithm.ES256)
			.compact();
	}

	private PrivateKey getApplePrivateKey() {

		// 1) .p8 파일 직접 사용하는 방법
		// ClassPathResource resource = new ClassPathResource("Apple_Developer_페이지에서_다운.p8");
		// String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
		// String privateKey = appleProperties.getPrivateKey();
		// Reader pemReader = new StringReader(privateKey);
		// PEMParser pemParser = new PEMParser(pemReader);
		// JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
		// PrivateKeyInfo object = (PrivateKeyInfo)pemParser.readObject();
		// return converter.getPrivateKey(object);

		// 2) .p8 안의 키 string 값을 사용하는 방법
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

		try {
			byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivateKey());

			PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
			return converter.getPrivateKey(privateKeyInfo);
		} catch (Exception e) {
			throw new RuntimeException("Error converting private key from String", e);
		}
	}

	private Claims getAppleClaims(PublicKey publicKey, String identityToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(publicKey)
				.build()
				.parseClaimsJws(identityToken)
				.getBody();    // apple 고유 계정 id, email 등 중요 요소 사용하면 된다.
		} catch (JwtException e) {
			throw new CustomException(Result.TOKEN_INVALID);
		}
	}
}
