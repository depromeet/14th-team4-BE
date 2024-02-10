package com.depromeet.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.depromeet.auth.dto.TokenResponse;
import com.depromeet.document.RestDocsTestSupport;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
class AuthControllerTest extends RestDocsTestSupport {

	@BeforeEach
	public void setup() {
		Cookie mockAccessTokenCookie = new Cookie("accessToken", "test_access_token");
		Cookie mockRefreshTokenCookie = new Cookie("refreshToken", "test_refresh_token");

		when(cookieService.createAccessTokenCookie(anyString())).thenReturn(mockAccessTokenCookie);
		when(cookieService.createRefreshTokenCookie(anyString())).thenReturn(mockRefreshTokenCookie);
	}

	@Test
	void socialLogin() throws Exception {
		// given
		TokenResponse tokenResponse = TokenResponse.builder()
			.accessToken("access_token")
			.refreshToken("refresh_token")
			.isFirst(true)
			.build();
		given(authService.kakaoLogin(anyString())).willReturn(tokenResponse);

		// when
		mockMvc.perform(
				get("/api/v1/auth/login")
					.param("provider", "kakao")
					.param("code", "test_code")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					queryParameters(
						parameterWithName("provider").description("로그인 제공자 (apple, kakao)"),
						parameterWithName("code").description("로그인 코드")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data").type(JsonFieldType.OBJECT).description("토큰 값"),
						fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("엑세스토큰"),
						fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레쉬토큰"),
						fieldWithPath("data.isFirst").type(JsonFieldType.BOOLEAN).description("첫 로그인 여부")
					)
				)
			);
	}


	@Test
	void refreshToken() throws Exception {
		// given
		TokenResponse tokenResponse = TokenResponse.builder().accessToken("access_token").refreshToken("refresh_token").isFirst(null).build();
		given(authService.reissueToken(any())).willReturn(tokenResponse);

		// when
		mockMvc.perform(
			post("/api/v1/auth/token/reissue")
				.header("Authorization-refresh","refresh_token")
				)
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestHeaders(
						headerWithName("Authorization-refresh").description("refreshToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data").type(JsonFieldType.OBJECT).description("토큰 값"),
						fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("엑세스토큰"),
						fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레쉬토큰"),
						fieldWithPath("data.isFirst").type(JsonFieldType.NULL).description("첫 로그인 여부")
					)
				)
			);
	}

	@Test
	void signup() throws Exception {
		// given
		doNothing().when(authService).signup(any());

		// when
		mockMvc.perform(
				post("/api/v1/auth/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer accessToken"))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestHeaders(
						headerWithName("Authorization").description("accessToken")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data").type(JsonFieldType.NULL).description("NULL")
					)
				)
			);
	}

	@Test
	public void logout() throws Exception {
		doNothing().when(authService).logout(any(), any(), any());

		mockMvc.perform(post("/api/v1/auth/logout"))
			.andExpect(status().isOk());
	}

	@Test
	void getTestToken() throws Exception {
		// given
		String accessToken = "access_token";
		String refreshToken = "refresh_token";
		Long userId = 1L;

		given(jwtService.createAccessToken(userId)).willReturn(accessToken);
		given(jwtService.createRefreshToken(userId)).willReturn(refreshToken);

		// when
		mockMvc.perform(
				get("/api/v1/auth/access-token/{userId}",1L)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					pathParameters(
						parameterWithName("userId").description("USER ID")
					),
					responseFields(
						fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
						fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("테스트용 accessToken"),
						fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("테스트용 refreshToken"),
						fieldWithPath("data.isFirst").type(JsonFieldType.NULL).description("NULL")
					)
				)
			);
	}

}
