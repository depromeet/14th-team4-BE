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
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final JwtService jwtService;
	private final CookieService cookieService;

	@Value("${front.dev.url}")
	private String frontDevUrl;

	@Value("${front.local.url}")
	private String frontLocalUrl;

	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;

	private static final String REQUEST_ENV_ATTRIBUTE = "request_env";
	private static final String AUTH_PATH = "/auth";
	private static final String DEV_ENVIRONMENT = "dev";
	private static final String LOCAL_ENVIRONMENT = "local";
	private static final String IS_FIRST_PARAM = "isFirst";



	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		String requestEnv = (session != null) ? (String) session.getAttribute(REQUEST_ENV_ATTRIBUTE) : LOCAL_ENVIRONMENT;
		String redirectUrl = (DEV_ENVIRONMENT.equals(requestEnv)) ? frontDevUrl : frontLocalUrl;

		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
		handleUserAuthentication(response, oAuth2User, redirectUrl);
	}

	private void handleUserAuthentication(HttpServletResponse response, CustomOAuth2User oAuth2User, String redirectUrl) throws IOException {
		String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
		String refreshToken = jwtService.createRefreshToken(oAuth2User.getUserId());

		response.addCookie(cookieService.createAccessTokenCookie(accessToken));
		response.addCookie(cookieService.createRefreshTokenCookie(refreshToken));

		String isFirstValue = oAuth2User.getUserRole() == Role.GUEST ? "True" : "False";
		String targetUrl = String.format("%s%s?accessToken=%s&refreshToken=%s&%s=%s",
			redirectUrl, AUTH_PATH, accessToken, refreshToken, IS_FIRST_PARAM, isFirstValue);

		response.sendRedirect(targetUrl);
	}
}
