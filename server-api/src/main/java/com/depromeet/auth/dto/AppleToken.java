package com.depromeet.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

public class AppleToken {

	@Getter
	@Builder
	public static class Request {
		private String code; // authorizationCode
		private String client_id;
		private String client_secret;
		private String grant_type;
		private String refreshToken;

		public static Request of(String authorizationCode, String clientId, String clientSecret
			, String grantType) {
			return Request.builder()
				.code(authorizationCode)
				.client_id(clientId)
				.client_secret(clientSecret)
				.grant_type(grantType)
				.build();
		}

		public static Request of(String refreshToken, String clientId, String clientSecret) {
			return Request.builder()
				.refreshToken(refreshToken)
				.client_id(clientId)
				.client_secret(clientSecret)
				.grant_type("refresh_token")
				.build();
		}
	}

	/**
	 * 원래는 애플에서 주는 accessToken 값으로 session 유지하겠지만,
	 * 우리는 앱 측에서 refresh_token 으로 session 유지해야함.
	 *
	 * 로그인 또는 인증이 필요할 때마다 refresh token을 통해
	 * access token을 재발급 받아보는 형식으로 refresh token이 유효한지 검증해주면 된다.
	 */
	@Getter
	public static class Response {
		@JsonProperty("access_token")
		private String accessToken;

		@JsonProperty("token_type")
		private String tokenType;

		@JsonProperty("expires_in")
		private Long expiresIn;

		@JsonProperty("refresh_token")
		private String refreshToken;

		@JsonProperty("id_token")
		private String idToken;
	}

}
