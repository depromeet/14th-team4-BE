package com.depromeet.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtService jwtTokenProvider;

	public String[] whiteListInSwagger() {
		return new String[] {"/swagger", "/swagger-ui/springfox.css", "/swagger-ui/swagger-ui-bundle.js",
			"/swagger-ui/springfox.js", "/swagger-ui/swagger-ui-standalone-preset.js", "/swagger-ui/swagger-ui.css",
			"/swagger-resources/configuration/ui", "/swagger-ui/favicon-32x32.png",
			"/swagger-resources/configuration/security", "/swagger-resources", "/v2/api-docs", "/swagger-ui/login.html",
			"/favicon.ico"};
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = resolveToken(request);
		// 정상 토큰이면 해당 토큰으로 Authentication을 가져와서 SecurityContext에 저장
		if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}

		return null;
	}
}


