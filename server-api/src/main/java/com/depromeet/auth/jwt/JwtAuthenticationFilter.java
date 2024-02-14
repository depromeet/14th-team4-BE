package com.depromeet.auth.jwt;

import static com.depromeet.config.SecurityConfig.*;

import java.io.IOException;
import java.util.Arrays;

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
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final RedisService redisService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			String accessToken = jwtService.resolveToken(request);
			log.info("request url : {}", request.getRequestURL());
			log.info("request token : {}", accessToken);
			if (StringUtils.hasText(accessToken) && !isTokenBlacklisted(accessToken)
				&& jwtService.isValidToken(accessToken)) {
				setSecurityContext(accessToken);
			}
		} catch (CustomException e) {
			request.setAttribute("error", e.getResult());
		} catch (Exception e) {
			log.info(e.getMessage());
			request.setAttribute("error", Result.FAIL);
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return Arrays.stream(PATTERNS).anyMatch(exclude -> request.getServletPath().startsWith(exclude));
	}

	private void setSecurityContext(String token) {
		Authentication authentication = jwtService.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private boolean isTokenBlacklisted(String token) {
		return redisService.getValues(token) != null;
	}
}


