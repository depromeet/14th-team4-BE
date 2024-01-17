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
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		if (request.getRequestURI().startsWith("/oauth2/authorization")) {
			String queryString = request.getQueryString();
			log.debug("queryString = "+ queryString);
			if (queryString != null) {
				String[] keyValuePair = queryString.split("=");
				String requestEnv = "dev";
				if (keyValuePair[0].equals("env") && keyValuePair.length > 1) {
					requestEnv = keyValuePair[1];
				}
				log.debug("requestEnv = "+ requestEnv);
				request.getSession().setAttribute("request_env", requestEnv);
			} else {
				request.getSession().setAttribute("request_env", "dev");
			}
		}
		filterChain.doFilter(request, response);
	}
}
