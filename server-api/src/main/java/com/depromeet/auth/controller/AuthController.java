package com.depromeet.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.annotation.AuthUser;
import com.depromeet.auth.dto.TokenResponse;
import com.depromeet.auth.jwt.JwtService;
import com.depromeet.auth.service.AuthService;
import com.depromeet.auth.service.CookieService;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.user.entity.User;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
	private final AuthService authService;
	private final CookieService cookieService;
	private final JwtService jwtService;

	/**
	 * Refresh Token으로 사용자 Access Token 갱신 요청
	 */
	@PostMapping("/token/reissue")
	public CustomResponseEntity<TokenResponse> refreshToken(
		@RequestHeader(value = "Authorization-refresh") String refreshToken, HttpServletResponse response
	) throws IllegalAccessException {
		TokenResponse tokenResponse = authService.reissueToken(refreshToken);
		// 응답 헤더에 쿠키 추가
		response.addCookie(cookieService.createAccessTokenCookie(tokenResponse.getAccessToken()));
		response.addCookie(cookieService.createRefreshTokenCookie(tokenResponse.getRefreshToken()));
		return CustomResponseEntity.success(tokenResponse);
	}

	@PostMapping("/signup")
	public CustomResponseEntity<Void> signup(@AuthUser User user, HttpServletResponse response
	) throws IllegalAccessException {
		authService.signup(user);
		return CustomResponseEntity.success();
	}

	/**
	 * 테스트용 accesstoken 발급
	 * 운영시 삭제 예정
	 */
	@GetMapping("/access-token/{userId}")
	public CustomResponseEntity<TokenResponse> getTestTokenByUserId(@PathVariable("userId") Long userId) {
		String accessToken = jwtService.createAccessToken(userId);
		String refreshToken = jwtService.createRefreshToken(userId);

		return CustomResponseEntity.success(new TokenResponse(accessToken, refreshToken));
	}
}
