package com.depromeet.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.auth.dto.TokenResponse;
import com.depromeet.auth.jwt.JwtService;
import com.depromeet.auth.service.AuthService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
	private final AuthService authService;
	private final JwtService jwtService;

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

	/**
	 * 테스트용 accesstoken 발급
	 * 운영시 삭제 예정
	 */
	@GetMapping("/access-token/{userId}")
	public TokenResponse getTokenByUserId(@PathVariable("userId") Long userId) {
		String accessToken = jwtService.createAccessToken(userId);
		String refreshToken = jwtService.createRefreshToken(userId);

		return new TokenResponse(accessToken, refreshToken);
	}
}
