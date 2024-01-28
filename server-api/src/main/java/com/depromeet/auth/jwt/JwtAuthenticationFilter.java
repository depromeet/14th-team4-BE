package com.depromeet.auth.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.depromeet.auth.service.RedisService;
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
	private final RedisService redisService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = jwtService.resolveToken(request);
			if (StringUtils.hasText(token) && isTokenValid(token)) {
				setSecurityContext(token);
			} else {
				request.setAttribute("error", Result.TOKEN_INVALID);
			}

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

	private boolean isTokenValid(String token) {
		return !isTokenBlacklisted(token) && jwtService.isValidToken(token);
	}

	private boolean isTokenBlacklisted(String token) {
		return redisService.getValues(token) != null;
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		return null;
	}

}


