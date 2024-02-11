package com.depromeet.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenResponse {
	String accessToken;
	String refreshToken;
	Boolean isFirst;
}
