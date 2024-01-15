package com.depromeet.auth.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithMockUser
class AuthControllerTest extends RestDocsTestSupport {

	@BeforeEach
	public void setup() {
		Cookie mockAccessTokenCookie = new Cookie("access_token", "test_access_token");
		Cookie mockRefreshTokenCookie = new Cookie("refresh_token", "test_refresh_token");

		when(cookieService.createAccessTokenCookie(anyString())).thenReturn(mockAccessTokenCookie);
		when(cookieService.createRefreshTokenCookie(anyString())).thenReturn(mockRefreshTokenCookie);
	}


	@Test
	void refreshToken() throws Exception {
		// given
		String refreshToken = "some_refresh_token";
		TokenResponse tokenResponse = new TokenResponse("new_access_token", "new_refresh_token");
		given(authService.reissueToken(refreshToken)).willReturn(tokenResponse);

		// when
		mockMvc.perform(
			post("/api/v1/auth/token/reissue")
				.header("Authorization-refresh", refreshToken))
			.andExpect(status().isOk())
			.andDo(
				restDocs.document(
					requestHeaders(
						headerWithName("Authorization-refresh").description("refreshToken")
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
	void signup() throws Exception {
		// given
		TokenResponse tokenResponse = new TokenResponse("access_token", "refresh_token");

		given(authService.signup(any())).willReturn(tokenResponse);

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
	void getTestTokenByUserId() {
	}

	@Test
	void oauth2login() {
	}

}
