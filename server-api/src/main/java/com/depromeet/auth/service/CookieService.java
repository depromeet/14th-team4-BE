package com.depromeet.auth.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CookieService {
	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;

	public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		log.info("cookie.length :" + cookies.length);
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				log.info("Cookie Name: " + cookie.getName() + ", Value: " + cookie.getValue());
				if (name.equals(cookie.getName())) {
					return Optional.of(cookie);
				}
			}
		}
		return Optional.empty();
	}

	public Cookie createAccessTokenCookie(String token) {
		Cookie cookie = new Cookie("accessToken", token);
		int accessTokenExpirationInSeconds = (int)(accessTokenExpirationPeriod / 1000);
		cookie.setMaxAge(accessTokenExpirationInSeconds);
		cookie.setDomain("ddoeat.site");
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		cookie.setSecure(true);
		return cookie;
	}

	public Cookie createRefreshTokenCookie(String token) {
		Cookie cookie = new Cookie("refreshToken", token);
		int refreshTokenExpirationInSeconds = (int)(refreshTokenExpirationPeriod / 1000);
		cookie.setMaxAge(refreshTokenExpirationInSeconds);
		cookie.setDomain("ddoeat.site");
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		return cookie;
	}

	public void deleteAccessTokenCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("accessToken", null);
		cookie.setMaxAge(0);
		cookie.setDomain("ddoeat.site");
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		cookie.setSecure(true);
		response.addCookie(cookie);
	}

	public void deleteRefreshTokenCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setMaxAge(0);
		cookie.setDomain("ddoeat.site");
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		response.addCookie(cookie);
	}
}
