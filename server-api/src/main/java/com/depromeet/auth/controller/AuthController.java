package com.depromeet.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.annotation.AuthUser;
import com.depromeet.auth.dto.SocialLoginRequest;
import com.depromeet.auth.dto.TokenResponse;
import com.depromeet.auth.jwt.JwtService;
import com.depromeet.auth.service.AuthService;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.user.entity.User;
import com.depromeet.enums.SocialType;

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

	@PostMapping("/login")
	public CustomResponseEntity<TokenResponse> socialLogin(
		@Valid @RequestBody SocialLoginRequest socialLoginRequest
	) {
		SocialType provider = SocialType.fromString(socialLoginRequest.getProvider());
		if (SocialType.KAKAO.equals(provider)) {
			return CustomResponseEntity.success(authService.kakaoLogin(socialLoginRequest.getCode(), socialLoginRequest.getRedirect_uri()));
		}
		return CustomResponseEntity.success(authService.appleLogin(socialLoginRequest.getCode()));
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

		return CustomResponseEntity.success(
			TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build());
	}
}
