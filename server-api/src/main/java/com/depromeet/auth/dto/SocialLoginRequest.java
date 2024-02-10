package com.depromeet.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SocialLoginRequest {

	@NotEmpty(message = "accessToken 값은 필수입니다.")
	private String accessToken;

	@NotEmpty(message = "refreshToken 값은 필수입니다.")
	private String refreshToken;
}
