package com.depromeet.auth.oauth2.handler;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationRequestFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		if (request.getRequestURI().startsWith("/oauth2/authorization")) {
			String queryString = request.getQueryString();
			if (queryString != null) {
				String[] keyValuePair = queryString.split("=");
				String requestEnv = "dev";
				if (keyValuePair[0].equals("app") && keyValuePair.length > 1) {
					requestEnv = keyValuePair[1];
				}
				request.getSession().setAttribute("request_env", requestEnv);
			}
		}
		filterChain.doFilter(request, response);
	}
}
