package com.depromeet.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.annotation.AuthUser;
import com.depromeet.auth.dto.TokenResponse;
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

	/**
	 * Refresh Token으로 사용자 Access Token 갱신 요청
	 */
	@PostMapping("/token/reissue")
	public ResponseEntity<TokenResponse> refreshToken(
		@RequestHeader(value = "Authorization-refresh") String refreshToken
	) throws IllegalAccessException {
		TokenResponse response = authService.reissueToken(refreshToken);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/signup")
	public CustomResponseEntity<Object> signup(@AuthUser User user, HttpServletResponse response
	) throws IllegalAccessException {
		TokenResponse tokenResponse = authService.signup(user.getUserId());
		// 응답 헤더에 쿠키 추가
		response.addCookie(cookieService.createAccessTokenCookie(tokenResponse.getAccessToken()));
		response.addCookie(cookieService.createRefreshTokenCookie(tokenResponse.getRefreshToken()));
		return CustomResponseEntity.success();
	}
}
