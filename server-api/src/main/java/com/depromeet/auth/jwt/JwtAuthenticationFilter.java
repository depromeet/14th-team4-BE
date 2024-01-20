package com.depromeet.auth.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = resolveToken(request);
			if (StringUtils.hasText(token)) {
				jwtService.isValidToken(token);
				setSecurityContext(token);
			}
			request.setAttribute("error", Result.TOKEN_INVALID);

		} catch (CustomException e) {
			request.setAttribute("error", e.getResult());
		} catch (Exception e) {
			request.setAttribute("error", Result.FAIL);
		}

		filterChain.doFilter(request, response);
	}

	private void setSecurityContext(String token) {
		Authentication authentication = jwtService.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		return null;
	}
}


