package com.depromeet.auth.oauth2.handler;

import java.io.IOException;

import com.depromeet.auth.oauth2.CustomOAuth2User;
import com.depromeet.auth.jwt.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final JwtService jwtService;
	@Value("${server.url}")
	private String serverUrl;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
		String refreshToken = jwtService.createRefreshToken(oAuth2User.getUserId());

		String redirectURL = serverUrl + "?access_token=" + accessToken + "&refresh_token=" + refreshToken;

		response.sendRedirect(redirectURL);
	}
}
