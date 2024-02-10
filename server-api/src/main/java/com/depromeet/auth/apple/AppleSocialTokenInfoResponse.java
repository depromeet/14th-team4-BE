package com.depromeet.auth.apple;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AppleSocialTokenInfoResponse {

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
