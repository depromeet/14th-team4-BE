package com.depromeet.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SocialSignupRequest {

	@NotEmpty(message = "identityToken 값은 필수입니다.")
	private String identityToken;

	@NotEmpty(message = "authorizationCode 값은 필수입니다.")
	private String authorizationCode;
}
