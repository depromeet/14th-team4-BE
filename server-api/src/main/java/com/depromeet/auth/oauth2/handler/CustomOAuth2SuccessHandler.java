package com.depromeet.auth.oauth2.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.depromeet.auth.jwt.JwtService;
import com.depromeet.auth.oauth2.CustomOAuth2User;
import com.depromeet.auth.service.CookieService;
import com.depromeet.enums.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final JwtService jwtService;
	private final CookieService cookieService;

	@Value("${front.url}")
	private String frontUrl;

	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		System.out.println(oAuth2User.getUserRole());
		if (oAuth2User.getUserRole() == Role.GUEST) {
			String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
			response.addCookie(cookieService.createAccessTokenCookie(accessToken));

			// 약관 동의 페이지로 리다이렉트
			String targetUrl = frontUrl + "/terms";
			response.sendRedirect(targetUrl);

		} else {
			loginSuccess(response, oAuth2User);
		}
	}

	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {

		String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
		String refreshToken = jwtService.createRefreshToken(oAuth2User.getUserId());

		// 응답 헤더에 쿠키 추가
		response.addCookie(cookieService.createAccessTokenCookie(accessToken));
		response.addCookie(cookieService.createRefreshTokenCookie(refreshToken));

		response.sendRedirect(frontUrl);
	}
}
