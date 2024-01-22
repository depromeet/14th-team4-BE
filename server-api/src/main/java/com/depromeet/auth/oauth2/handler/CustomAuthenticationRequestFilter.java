package com.depromeet.auth.oauth2.handler;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationRequestFilter extends OncePerRequestFilter {
	private static final String OAUTH2_AUTHORIZATION_PREFIX = "/oauth2/authorization";
	private static final String REQUEST_ENV_SESSION_ATTRIBUTE = "request_env";
	private static final String DEFAULT_REQUEST_ENV = "dev";
	private static final String ENV_QUERY_PARAM = "env";
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		if (request.getRequestURI().startsWith(OAUTH2_AUTHORIZATION_PREFIX)) {
			String queryString = request.getQueryString();

			String requestEnv = DEFAULT_REQUEST_ENV;
			if (queryString != null && queryString.startsWith(ENV_QUERY_PARAM + "=")) {
				String[] keyValuePair = queryString.split("=");
				if (keyValuePair.length > 1) {
					requestEnv = keyValuePair[1];
				}
			}
			request.getSession().setAttribute(REQUEST_ENV_SESSION_ATTRIBUTE, requestEnv);
		}
		filterChain.doFilter(request, response);
	}
}
