package com.depromeet.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.annotation.AuthUser;
import com.depromeet.auth.dto.SocialLoginRequest;
import com.depromeet.auth.dto.SocialSignupRequest;
import com.depromeet.auth.dto.TokenResponse;
import com.depromeet.auth.jwt.JwtService;
import com.depromeet.auth.service.AuthService;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.user.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
	private final AuthService authService;
	private final JwtService jwtService;

	@GetMapping("/login")
	public CustomResponseEntity<TokenResponse> socialLogin(
		@RequestParam String provider,
		@RequestParam String code
	) {
		// TODO: provider에 따라서 로그인 처리
		if ("kakao".equals(provider)) {
			return CustomResponseEntity.success(authService.kakaoLogin(code));
		}
		return CustomResponseEntity.success(authService.kakaoLogin(code));
	}

	@PostMapping("/{social}/signup")
	public CustomResponseEntity<TokenResponse> socialSignup(
		@Valid @RequestBody SocialSignupRequest signupRequest, @PathVariable("social") String social) {

		TokenResponse tokenResponse = null;

		if ("apple".equalsIgnoreCase(social)) {
			// todo - 애플에서는 첫 로그인? 에서만 user 정보를 준다는데 테스트 해보고 저장할 것 추가하기
			tokenResponse
				= this.authService.signupWithApple(signupRequest.getIdentityToken(),
				signupRequest.getAuthorizationCode());
		} else {
			// todo - 카카오
		}

		return CustomResponseEntity.success(tokenResponse);
	}

	@PostMapping("/{social}/login")
	public CustomResponseEntity<TokenResponse> socialLogin(
		@Valid @RequestBody SocialLoginRequest loginRequest, @PathVariable("social") String social) {

		TokenResponse tokenResponse = null;

		if ("apple".equalsIgnoreCase(social)) {
			return CustomResponseEntity.success(authService.loginWithApple(loginRequest));
		} else {
			// todo - 카카오
		}

		return CustomResponseEntity.success(tokenResponse);
	}

	/**
	 * Refresh Token으로 사용자 Access Token 갱신 요청
	 */
	@PostMapping("/token/reissue")
	public CustomResponseEntity<TokenResponse> refreshToken(
		@RequestHeader("Authorization-refresh") String refreshToken
	) {
		TokenResponse tokenResponse = authService.reissueToken(refreshToken);
		return CustomResponseEntity.success(tokenResponse);
	}

	@PostMapping("/signup")
	public CustomResponseEntity<Void> signup(@AuthUser User user, HttpServletResponse response
	) {
		authService.signup(user);
		return CustomResponseEntity.success();
	}

	@PostMapping("/logout")
	public void logout(@AuthUser User user, HttpServletRequest request, HttpServletResponse response) {
		authService.logout(user, request, response);
	}

	/**
	 * 테스트용 accesstoken 발급
	 * 운영시 삭제 예정
	 */
	@GetMapping("/access-token/{userId}")
	public CustomResponseEntity<TokenResponse> getTestTokenByUserId(@PathVariable("userId") Long userId,
		HttpServletResponse response) {
		String accessToken = jwtService.createAccessToken(userId);
		String refreshToken = jwtService.createRefreshToken(userId);

		return CustomResponseEntity.success(TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build());
	}
}
