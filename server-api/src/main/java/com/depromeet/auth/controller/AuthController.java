package com.depromeet.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.auth.dto.TokenResponse;
import com.depromeet.auth.service.AuthService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
	private final AuthService authService;

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
}
