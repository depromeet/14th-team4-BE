package com.depromeet.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppleLoginResponse {

	private String accessToken;
	private String refreshToken;
	private boolean isFirstValue;

	public static AppleLoginResponse of(String accessToken, String refreshToken
		, boolean isFirstValue) {
		return AppleLoginResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.isFirstValue(isFirstValue)
			.build();
	}
}
