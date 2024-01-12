package com.depromeet.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
public class CookieService {
	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;

	public Cookie createAccessTokenCookie(String token) {
		Cookie cookie = new Cookie("accessToken", token);
		cookie.setPath("/");
		int accessTokenExpirationInSeconds = (int)(accessTokenExpirationPeriod / 1000);
		cookie.setMaxAge(accessTokenExpirationInSeconds);
		cookie.setHttpOnly(true);
		return cookie;
	}

	public Cookie createRefreshTokenCookie(String token) {
		Cookie cookie = new Cookie("refreshToken", token);
		cookie.setPath("/");
		int accessTokenExpirationInSeconds = (int)(refreshTokenExpirationPeriod / 1000);
		cookie.setMaxAge(accessTokenExpirationInSeconds);
		cookie.setHttpOnly(true);
		return cookie;
	}
}
