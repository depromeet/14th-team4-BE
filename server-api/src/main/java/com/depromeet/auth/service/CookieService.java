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
		int accessTokenExpirationInSeconds = (int)(accessTokenExpirationPeriod / 1000);
		cookie.setMaxAge(accessTokenExpirationInSeconds);
		cookie.setDomain(".ddoeat.site");
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		return cookie;
	}

	public Cookie createRefreshTokenCookie(String token) {
		Cookie cookie = new Cookie("refreshToken", token);
		int refreshTokenExpirationInSeconds = (int)(refreshTokenExpirationPeriod / 1000);
		cookie.setMaxAge(refreshTokenExpirationInSeconds);
		cookie.setDomain(".ddoeat.site");
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		return cookie;
	}
}
