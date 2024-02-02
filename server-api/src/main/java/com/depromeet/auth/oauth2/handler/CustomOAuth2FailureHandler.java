package com.depromeet.auth.oauth2.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.setStatus(Result.SOCIAL_LOGIN_FAIL.getCode());
		ObjectMapper objectMapper = new ObjectMapper();
		response.getWriter().write(objectMapper.writeValueAsString(
			new CustomException(Result.SOCIAL_LOGIN_FAIL)
		));
		log.info("소셜 로그인에 실패했습니다. 에러 메시지 : {}", exception.getMessage());
	}
}
