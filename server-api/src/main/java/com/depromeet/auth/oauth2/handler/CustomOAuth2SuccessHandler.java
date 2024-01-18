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


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		// String requestEnv = (String) session.getAttribute("request_env")
		log.debug("session in successhandler" + session);
		String redirectUrl = "";
		String requestEnv = (session != null) ? (String) session.getAttribute("request_env") : "local";
		log.debug("requestEnv in successhandler" + requestEnv);

		if (requestEnv == null) {
			redirectUrl = frontLocalUrl;
		} else if (requestEnv.equals("dev")) {
			redirectUrl = frontDevUrl;
		} else {
			redirectUrl = frontLocalUrl;
		}

		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		if (oAuth2User.getUserRole() == Role.GUEST) {
			String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
			String refreshToken = jwtService.createRefreshToken(oAuth2User.getUserId());

			response.addCookie(cookieService.createAccessTokenCookie(accessToken));
			response.addCookie(cookieService.createRefreshTokenCookie(refreshToken));
			// response.addHeader("Set-Cookie", cookieService.createAccessTokenCookie(accessToken).toString());
			// response.addHeader("Set-Cookie", cookieService.createRefreshTokenCookie(refreshToken).toString());

			String targetUrl = redirectUrl + "/terms" + "?accessToken=" +accessToken + "&refreshToken=" + refreshToken ;
			log.debug(targetUrl);
			response.sendRedirect(targetUrl);
		} else {
			loginSuccess(response, oAuth2User, redirectUrl);
		}
	}

	private String determineRedirectUrl(String requestEnv) {
		return switch (requestEnv) {
			case "dev" -> frontDevUrl;
			case "local" -> frontLocalUrl;
			default -> frontDevUrl; // Default URL
		};
	}

	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User, String redirectUrl) throws IOException {

		String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
		String refreshToken = jwtService.createRefreshToken(oAuth2User.getUserId());

		// 응답 헤더에 쿠키 추가
		response.addHeader("Set-Cookie", cookieService.createAccessTokenCookie(accessToken).toString());
		response.addHeader("Set-Cookie", cookieService.createRefreshTokenCookie(refreshToken).toString());
		String targetUrl = redirectUrl + "?accessToken=" +accessToken + "&refreshToken=" + refreshToken ;
		response.sendRedirect(targetUrl);
	}
}
